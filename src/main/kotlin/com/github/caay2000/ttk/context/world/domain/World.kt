package com.github.caay2000.ttk.context.world.domain

import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.shared.Aggregate
import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.LocationId
import com.github.caay2000.ttk.shared.WorldId
import com.github.caay2000.ttk.shared.randomDomainId
import com.github.caay2000.ttk.shared.replace

data class World(
    override val id: WorldId = randomDomainId(),
    val currentTurn: Int,
    val cells: Set<Cell>,
    val companies: Map<CompanyId, Company>
) : Aggregate() {

    val connectedCells: Set<Cell>
        get() = cells.filter { it.connected }.toSet()

    companion object {
        fun create(width: Int, height: Int) = World(
            currentTurn = 0,
            cells = createCells(width, height),
            companies = emptyMap()
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

    fun update(): World = copy(currentTurn = currentTurn + 1)

    fun createConnection(path: Set<Cell>): World =
        path.fold(initial = this) { world, cell -> world.updateCell { cell } }

    fun addLocation(locationId: LocationId, position: Position): World =
        updateCell { getCell(position).updateLocationId(locationId) }

    private fun updateCell(cell: () -> Cell): World = cell().let { newCell ->
        copy(cells = cells.replace(predicate = { it.samePosition(newCell) }, operation = { newCell }).toSet())
    }
}
