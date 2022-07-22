package com.github.caay2000.ttk.domain.entity

import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId

data class Entity(
    val id: EntityId,
    val currentPosition: Position,
    val destination: Position
) {

    companion object {
        fun create(id: EntityId = randomDomainId(), position: Position) = Entity(id = id, currentPosition = position, destination = position)
        fun create(id: EntityId = randomDomainId(), x: Int, y: Int) = Entity(id = id, currentPosition = Position(x, y), destination = Position(x, y))
    }

    val finished: Boolean = currentPosition == destination
}
