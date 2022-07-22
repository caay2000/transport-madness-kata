package com.github.caay2000.ttk.mother

import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.world.Cell
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId

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

    fun oneVehicle(entityId: EntityId = randomDomainId()): World =
        EntityMother.random(id = entityId).let {
            empty(
                entities = mapOf(it.id to it)
            )
        }

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
