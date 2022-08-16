package com.github.caay2000.ttk.mother.world

import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position

object CellMother {

    fun random(
        x: Int = 0,
        y: Int = 0,
        cost: Cell.CellConnection = Cell.CellConnection.NOT_CONNECTED
    ) = Cell(
        position = Position(x, y),
        connection = cost
    )
}
