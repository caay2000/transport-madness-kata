package com.github.caay2000.ttk.application.entity.movement

import com.github.caay2000.ttk.domain.world.Position

class SimpleMovementStrategy : MovementStrategy {

    private enum class Movement(val position: Position) {
        NORTH(Position(0, -1)),
        EAST(Position(1, 0)),
        SOUTH(Position(0, 1)),
        WEST(Position(-1, 0))
    }

    override fun move(source: Position, destination: Position): Position =
        Movement.values().fold(initial = source) { result, movement ->
            val step = source.sum(movement.position)
            if (step.distance(destination) < result.distance(destination)) step
            else result
        }
}
