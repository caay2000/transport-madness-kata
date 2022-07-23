package com.github.caay2000.ttk.domain.entity

import com.github.caay2000.ttk.application.entity.movement.MovementStrategy
import com.github.caay2000.ttk.application.entity.movement.SimpleMovementStrategy
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId

data class Entity(
    val id: EntityId,
    val currentPosition: Position,
    val destination: Position,
    val movementStrategy: MovementStrategy = SimpleMovementStrategy()
) {
    fun setDestination(position: Position) = copy(destination = position)

    fun update(): Entity =
        if (currentPosition != destination) {
            copy(currentPosition = movementStrategy.move(currentPosition, destination))
        } else this

    companion object {
        fun create(id: EntityId = randomDomainId(), position: Position) = Entity(id = id, currentPosition = position, destination = position)
    }

    val finished: Boolean = currentPosition == destination
}
