package com.github.caay2000.ttk.domain.world

import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.LocationId
import com.github.caay2000.ttk.shared.replace

data class World(
    val currentTurn: Int,
    val cells: Set<Cell>,
    val entities: Map<EntityId, Entity>,
    val locations: Map<LocationId, Location>
) {

    val connectedCells: Set<Cell>
        get() = cells.filter { it.connected }.toSet()

    companion object {
        fun create(width: Int, height: Int) = World(0, createCells(width, height), emptyMap(), emptyMap())

        private fun createCells(width: Int, height: Int): Set<Cell> {
            val cells = mutableSetOf<Cell>()
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
    fun refreshEntity(entity: Entity): World = putEntity(entity)

    fun getLocation(id: LocationId): Location = locations.getValue(id)
    fun putLocation(location: Location): World =
        updateCell { getCell(location.position).updateLocationId(location.id) }
            .copy(locations = locations + (location.id to location))

    fun refreshLocation(location: Location): World =
        copy(locations = locations + (location.id to location))

    fun update(): World = copy(currentTurn = currentTurn + 1)

    fun createConnection(path: Set<Cell>): World =
        path.fold(initial = this) { world, cell -> world.updateCell { cell } }

    private fun updateCell(cell: () -> Cell): World = cell().let { cell ->
        copy(cells = cells.replace(predicate = { it.samePosition(cell) }, operation = { cell }).toSet())
    }
}
