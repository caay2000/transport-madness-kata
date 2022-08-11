package com.github.caay2000.ttk.context.entity.domain

import com.github.caay2000.ttk.api.event.DomainQueryExecutor
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.configuration.query.GetConfigurationQuery
import com.github.caay2000.ttk.context.entity.event.EntityLoadedEvent
import com.github.caay2000.ttk.context.entity.event.EntityUnloadedEvent
import com.github.caay2000.ttk.context.entity.query.EntityNextSectionQuery
import com.github.caay2000.ttk.context.location.query.LocationPassengerAvailableQuery
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.shared.Aggregate
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId

data class Entity(
    override val id: EntityId,
    val currentPosition: Position,
    val currentDuration: Int,
    val route: Route,
    val status: EntityStatus = EntityStatus.STOP,
    val pax: Int
) : Aggregate() {

    private val configuration by lazy { getConfigurationQueryExecution() }

    companion object {
        fun create(id: EntityId = randomDomainId(), position: Position): Entity = Entity(
            id = id,
            currentPosition = position,
            currentDuration = 0,
            route = Route.create(listOf(position)),
            pax = 0
        )
    }

    private val currentDestination: Position
        get() = route.currentDestination

    private val destinationReached: Boolean
        get() = currentPosition == currentDestination && status == EntityStatus.IN_ROUTE

    private val shouldResumeRoute: Boolean
        get() = currentDuration > configuration.turnsStoppedInStation && status == EntityStatus.STOP

    private val shouldUpdateNextSection: Boolean
        get() = route.nextSectionList.isEmpty() && status == EntityStatus.IN_ROUTE

    private val shouldMove: Boolean
        get() = status == EntityStatus.IN_ROUTE

    fun assignRoute(route: Route) = copy(route = route)

    fun update(): Entity =
        increaseDuration()
            .loadPassengers()
            .resumeRoute()
            .refreshNextSection()
            .moveEntity()
            .stopEntity()
            .unloadPassengers()

    private fun increaseDuration(): Entity = copy(currentDuration = currentDuration + 1)

    private fun unloadPassengers(): Entity =
        if (status == EntityStatus.STOP && currentDuration == 0 && pax > 0) {
            copy(pax = 0).also { it.pushEvent(EntityUnloadedEvent(id, pax, currentPosition)) }
        } else this

    private fun loadPassengers(): Entity =
        if (status == EntityStatus.STOP && currentDuration == 1) {
            val loadedPAX = locationPassengerAvailableQueryExecution(currentPosition)
            if (loadedPAX > 0) {
                copy(pax = pax + loadedPAX).also {
                    it.pushEvents(pullEvents() + EntityLoadedEvent(id, loadedPAX, currentPosition))
                }
            } else this
        } else this

    private fun resumeRoute(): Entity =
        if (shouldResumeRoute) copy(route = route.nextStop(), status = EntityStatus.IN_ROUTE, currentDuration = 0)
        else this

    private fun refreshNextSection(): Entity =
        if (shouldUpdateNextSection) copy(route = route.updateNextSection(entityNextSectionQueryExecution(currentPosition, currentDestination)))
        else this

    private fun moveEntity(): Entity =
        if (shouldMove) copy(currentPosition = route.nextSection.position, route = route.dropNextSection())
        else this

    private fun stopEntity(): Entity =
        if (destinationReached) copy(status = EntityStatus.STOP, currentDuration = 0)
        else this

    private fun getConfigurationQueryExecution(): Configuration =
        DomainQueryExecutor.execute<GetConfigurationQuery, GetConfigurationQuery.Response>(GetConfigurationQuery()).configuration

    private fun entityNextSectionQueryExecution(currentPosition: Position, currentDestination: Position): List<Cell> =
        DomainQueryExecutor.execute<EntityNextSectionQuery, EntityNextSectionQuery.Response>(EntityNextSectionQuery(currentPosition, currentDestination)).path

    private fun locationPassengerAvailableQueryExecution(currentPosition: Position): Int =
        DomainQueryExecutor.execute<LocationPassengerAvailableQuery, LocationPassengerAvailableQuery.Response>(LocationPassengerAvailableQuery(currentPosition)).available
}
