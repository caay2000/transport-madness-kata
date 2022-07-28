package com.github.caay2000.ttk.mother.world

import com.github.caay2000.ttk.domain.world.Cell
import com.github.caay2000.ttk.domain.world.Position

object CellMother {

    fun random(
        x: Int = 0,
        y: Int = 0,
        cost: Int = 0
    ) = Cell(
        position = Position(x, y),
        cost = cost
    )
}
