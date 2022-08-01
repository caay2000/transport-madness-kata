package com.github.caay2000.ttk.domain.entity

import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.world.Cell
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId

data class Entity(
    val id: EntityId,
    val currentPosition: Position,
    val currentDuration: Int,
    val route: Route,
    val status: EntityStatus = EntityStatus.STOP,
    @Transient
    val configuration: Configuration
) {

    companion object {
        fun create(id: EntityId = randomDomainId(), position: Position, configuration: Configuration): Entity = Entity(
            id = id,
            currentPosition = position,
            currentDuration = 0,
            route = Route.create(listOf(position)),
            configuration = configuration
        )
    }

    val currentDestination: Position
        get() = route.currentDestination

    val nextDestination: Position
        get() = route.nextDestination

    val shouldResumeRoute: Boolean
        get() = currentDuration >= configuration.turnsStoppedInStation && status == EntityStatus.STOP

    val shouldUpdateNextSection: Boolean
        get() = shouldResumeRoute || (route.nextSection.isEmpty() && status == EntityStatus.IN_ROUTE)

    fun assignRoute(route: Route) = copy(route = route)

    fun update(): Entity = when (status) {
        EntityStatus.STOP -> updateInStop()
        EntityStatus.IN_ROUTE -> updateInRoute()
    }

    fun updateNextSection(nextSection: List<Cell>): Entity =
        copy(route = route.copy(nextSection = nextSection))

    private fun updateInStop(): Entity = when {
        shouldResumeRoute -> copy(route = route.nextStop(), status = EntityStatus.IN_ROUTE, currentDuration = 0).updateInRoute()
        else -> copy(currentDuration = currentDuration + 1)
    }

    private fun updateInRoute(): Entity {
        val (nextPosition, route) = route.popNextSection()
        val stopReached = nextPosition.position == route.currentDestination
        return if (stopReached) {
            copy(currentPosition = nextPosition.position, status = EntityStatus.STOP, currentDuration = 0, route = route)
        } else copy(currentPosition = nextPosition.position, currentDuration = currentDuration + 1, route = route)
    }
}
