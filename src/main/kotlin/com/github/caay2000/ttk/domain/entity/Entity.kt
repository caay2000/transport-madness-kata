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

//    private val movementStrategy: MovementStrategy = SimpleMovementStrategy()

    companion object {
        fun create(id: EntityId = randomDomainId(), position: Position, configuration: Configuration): Entity = Entity(
            id = id,
            currentPosition = position,
            currentDuration = 0,
            route = Route.create(listOf(position)),
            configuration = configuration
        )
    }

    fun setRoute(route: Route) = copy(route = route)

    fun update(): Entity = when (status) {
        EntityStatus.STOP -> updateInStop()
        EntityStatus.IN_ROUTE -> updateInRoute()
    }

    val shouldResumeRoute: Boolean
        get() = currentDuration >= configuration.turnsStoppedInStation && status == EntityStatus.STOP

    val currentDestination: Position
        get() = route.currentDestination

    val nextDestination: Position
        get() = route.nextDestination

    private fun updateInStop(): Entity {
        return when {
            shouldResumeRoute -> copy(route = route.nextStop(), status = EntityStatus.IN_ROUTE, currentDuration = 0).updateInRoute()
            else -> copy(currentDuration = currentDuration + 1)
        }
    }

    val shouldUpdateNextSection: Boolean
        get() = shouldResumeRoute || (route.nextSection.isEmpty() && this.status == EntityStatus.IN_ROUTE)

    fun updateNextSection(nextSection: List<Cell>): Entity =
        copy(route = route.copy(nextSection = nextSection))

    private fun updateInRoute(): Entity {
        val nextPosition = route.nextSection.first()
        val route = route.copy(nextSection = route.nextSection - nextPosition)

//        val nextPosition = route.nextSection..move(currentPosition, route.currentDestination)
        val stopReached = nextPosition.position == route.currentDestination
        return if (stopReached) {
            copy(currentPosition = nextPosition.position, status = EntityStatus.STOP, currentDuration = 0, route = route)
        } else copy(currentPosition = nextPosition.position, currentDuration = currentDuration + 1, route = route)
    }
}
