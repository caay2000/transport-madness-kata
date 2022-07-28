package com.github.caay2000.ttk.application.entity.movement

import com.github.caay2000.ttk.domain.world.Position

interface MovementStrategy {

    fun move(source: Position, destination: Position): Position
}
