package com.github.caay2000.ttk.mother

import com.github.caay2000.ttk.domain.world.Cell
import com.github.caay2000.ttk.domain.world.World

object WorldMother {

    fun empty(width: Int, height: Int): World =
        World(
            currentTurn = 0,
            cells = createCells(width, height),
            entities = emptyList()
        )

    private fun createCells(width: Int, height: Int): List<Cell> {
        val cells = mutableListOf<Cell>()
        for (x in 0 until width) {
            for (y in 0 until height) {
                cells.add(Cell(x, y))
            }
        }
        return cells
    }
}
