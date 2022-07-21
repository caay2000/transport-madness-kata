package com.github.caay2000.ttk.domain.world

import com.github.caay2000.ttk.domain.entity.Entity

data class World(
    val currentTurn: Int,
    val cells: List<Cell>,
    val entities: List<Entity>
) {

    companion object {
        fun create(width: Int, height: Int) = World(0, createCells(width, height), emptyList())

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

    fun getCell(position: Position): Cell = cells.first { it.position == position }

    fun update(): World = copy(currentTurn = currentTurn + 1)
    fun addEntity(entity: Entity): World = copy(entities = entities + entity)
}
