package com.github.caay2000.ttk.context.world.domain

import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.shared.Aggregate
import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.WorldId
import com.github.caay2000.ttk.shared.randomDomainId
import com.github.caay2000.ttk.shared.replace

data class World(
    override val id: WorldId = randomDomainId(),
    val currentTurn: Int,
    val cells: Set<Cell>,
    val companies: Map<CompanyId, Company>,
    val entities: Map<EntityId, Entity>
//    ,
//    val locations: Map<LocationId, Location>
) : Aggregate() {

    val connectedCells: Set<Cell>
        get() = cells.filter { it.connected }.toSet()

    companion object {
        fun create(width: Int, height: Int) = World(
            currentTurn = 0,
            cells = createCells(width, height),
            companies = emptyMap(),
            entities = emptyMap()
//            ,
//            locations = emptyMap()
        )

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

//    fun getLocation(id: LocationId): Location = locations.getValue(id)
//    fun getLocation(position: Position): Location = locations.values.first { it.position == position }
//    fun addLocation(location: Location): World =
//        updateCell { getCell(location.position).updateLocationId(location.id) }
//            .copy(locations = locations + (location.id to location))
//
//    fun updateLocation(location: Location): World =
//        copy(locations = locations + (location.id to location))

    fun addCompany(company: Company): World = copy(companies = companies + (company.id to company))

    fun update(): World = copy(currentTurn = currentTurn + 1)

    fun createConnection(path: Set<Cell>): World =
        path.fold(initial = this) { world, cell -> world.updateCell { cell } }

    private fun updateCell(cell: () -> Cell): World = cell().let { newCell ->
        copy(cells = cells.replace(predicate = { it.samePosition(newCell) }, operation = { newCell }).toSet())
    }
}
