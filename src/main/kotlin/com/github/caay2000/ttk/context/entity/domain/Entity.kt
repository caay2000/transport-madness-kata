package com.github.caay2000.ttk.context.entity.domain

import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.entity.domain.update.LoadPassengersStrategy
import com.github.caay2000.ttk.context.entity.domain.update.NextSectionStrategy
import com.github.caay2000.ttk.context.entity.event.EntityUnloadedEvent
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
    val pax: Int,
    private val configuration: Configuration
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
        get() = isInRoute && currentPosition == currentDestination

    private val shouldResumeRoute: Boolean
        get() = isStopped && currentDuration > configuration.turnsStoppedInStation

    private val shouldMove: Boolean
        get() = isInRoute && isRouteAssigned

    private val shouldUpdateNextSection: Boolean
        get() = route.nextSectionList.isEmpty()

    private val isRouteAssigned: Boolean
        get() = route.stops.size > 1

    private val isInRoute: Boolean
        get() = status == EntityStatus.IN_ROUTE

    private val isStopped: Boolean
        get() = status == EntityStatus.STOP

    fun assignRoute(route: Route) = copy(route = route)

    fun update(): Entity = copy(currentDuration = currentDuration + 1)

    fun updateLoad(loadPassengersStrategy: LoadPassengersStrategy): Entity =
        if (isStopped && currentDuration == 1) {
            loadPassengersStrategy.invoke(this)
        } else this

    fun updateStart(): Entity =
        if (shouldResumeRoute) copy(route = route.nextStop(), status = EntityStatus.IN_ROUTE, currentDuration = 0)
        else this

    fun updateMove(nextSectionStrategy: NextSectionStrategy): Entity =
        if (shouldMove) {
            val entity = if (shouldUpdateNextSection) nextSectionStrategy.invoke(this) else this
            entity.copy(currentPosition = entity.route.nextSection.position, route = entity.route.dropNextSection())
        } else this

    fun updateStop(): Entity =
        if (destinationReached) copy(status = EntityStatus.STOP, currentDuration = 0)
        else this

    fun updateUnload(): Entity =
        if (isStopped && currentDuration == 0 && pax > 0) {
            copy(pax = 0).also { it.pushEvent(EntityUnloadedEvent(id, pax, currentPosition)) }
        } else this
}
