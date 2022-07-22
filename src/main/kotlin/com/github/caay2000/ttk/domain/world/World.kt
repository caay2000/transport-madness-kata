package com.github.caay2000.ttk.domain.world

import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.shared.EntityId

data class World(
    val currentTurn: Int,
    val cells: List<Cell>,
    val entities: Map<EntityId, Entity>
) {

    companion object {
        fun create(width: Int, height: Int) = World(0, createCells(width, height), emptyMap())

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

    fun getEntity(id: EntityId): Entity = entities.getValue(id)
    fun putEntity(entity: Entity): World = copy(entities = entities + (entity.id to entity))

    fun update(): World = copy(currentTurn = currentTurn + 1)
}
