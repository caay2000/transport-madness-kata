package com.github.caay2000.ttk.domain.entity

import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.entity.event.EntityLoadedEvent
import com.github.caay2000.ttk.domain.entity.event.EntityUnloadedEvent
import com.github.caay2000.ttk.domain.pathifinding.NextSectionFinder
import com.github.caay2000.ttk.domain.pathifinding.PassengerLoadingSystem
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.infra.eventbus.domain.Aggregate
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId

data class Entity(
    override val id: EntityId,
    val currentPosition: Position,
    val currentDuration: Int,
    val route: Route,
    val status: EntityStatus = EntityStatus.STOP,
    val pax: Int,
    @Transient
    val configuration: Configuration
) : Aggregate() {

    companion object {
        fun create(id: EntityId = randomDomainId(), position: Position, configuration: Configuration): Entity = Entity(
            id = id,
            currentPosition = position,
            currentDuration = 0,
            route = Route.create(listOf(position)),
            pax = 0,
            configuration = configuration
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

    fun update(nextSectionFinder: NextSectionFinder, loadingSystem: PassengerLoadingSystem): Entity =
        increaseDuration()
            .loadPassengers(loadingSystem)
            .resumeRoute()
            .refreshNextSection(nextSectionFinder)
            .moveEntity()
            .stopEntity()
            .unloadPassengers()

    private fun increaseDuration(): Entity = copy(currentDuration = currentDuration + 1)

    private fun unloadPassengers(): Entity =
        if (status == EntityStatus.STOP && currentDuration == 0 && pax > 0) {
            copy(pax = 0).also { it.pushEvent(EntityUnloadedEvent(id, pax, currentPosition)) }
        } else this

    private fun loadPassengers(loadingSystem: PassengerLoadingSystem): Entity =
        if (status == EntityStatus.STOP && currentDuration == 1) {
            copy(pax = pax + loadingSystem.invoke(currentPosition)).also {
                it.pushEvents(pullEvents() + EntityLoadedEvent(id, it.pax, currentPosition))
            }
        } else this

    private fun resumeRoute(): Entity =
        if (shouldResumeRoute) copy(route = route.nextStop(), status = EntityStatus.IN_ROUTE, currentDuration = 0)
        else this

    private fun refreshNextSection(nextSectionFinder: NextSectionFinder): Entity =
        if (shouldUpdateNextSection) copy(route = route.updateNextSection(nextSectionFinder.invoke(currentPosition, currentDestination)))
        else this

    private fun moveEntity(): Entity =
        if (shouldMove) copy(currentPosition = route.nextSection.position, route = route.dropNextSection())
        else this

    private fun stopEntity(): Entity =
        if (destinationReached) copy(status = EntityStatus.STOP, currentDuration = 0)
        else this
}
