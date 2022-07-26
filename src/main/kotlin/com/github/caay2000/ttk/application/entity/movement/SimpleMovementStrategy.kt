package com.github.caay2000.ttk.application.entity.movement

import com.github.caay2000.ttk.domain.world.Position

class SimpleMovementStrategy : MovementStrategy {

    override fun move(source: Position, destination: Position): Position =
        source.neighbours.fold(initial = source) { result, position ->
            if (position.distance(destination) < result.distance(destination)) position
            else result
        }
}
