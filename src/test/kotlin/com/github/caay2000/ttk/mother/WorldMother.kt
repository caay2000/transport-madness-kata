package com.github.caay2000.ttk.mother

import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.world.Cell
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.shared.EntityId

object WorldMother {

    fun empty(
        width: Int = 6,
        height: Int = 6,
        entities: Map<EntityId, Entity> = emptyMap()
    ): World =
        World(
            currentTurn = 0,
            cells = createCells(width, height),
            entities = entities
        )

    fun oneVehicle(entity: Entity = EntityMother.random()): World =
        empty(entities = mapOf(entity.id to entity))

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
