package com.github.caay2000.ttk.domain.entity

import com.github.caay2000.ttk.application.entity.movement.MovementStrategy
import com.github.caay2000.ttk.application.entity.movement.SimpleMovementStrategy
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId

data class Entity(
    val id: EntityId,
    val currentPosition: Position,
    val currentDuration: Int,
    val route: Route,
    val status: EntityStatus = EntityStatus.STOP,
    val configuration: Configuration,
    val movementStrategy: MovementStrategy = SimpleMovementStrategy()
) {

    companion object {
        fun create(id: EntityId = randomDomainId(), position: Position, configuration: Configuration) = Entity(
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

    private fun updateInStop(): Entity {
        return when {
            currentDuration < configuration.turnsStoppedInStation -> copy(currentDuration = currentDuration + 1)
            else -> copy(route = route.nextStop(), status = EntityStatus.IN_ROUTE, currentDuration = 0).updateInRoute()
        }
    }

    private fun updateInRoute(): Entity {
        val nextPosition = movementStrategy.move(currentPosition, route.currentDestination)
        val stopReached = nextPosition == route.currentDestination
        return if (stopReached) {
            copy(currentPosition = nextPosition, status = EntityStatus.STOP, currentDuration = 0)
        } else copy(currentPosition = nextPosition, currentDuration = currentDuration + 1)
    }
}
