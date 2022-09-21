package com.github.caay2000.ttk.context.world.domain

import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.shared.Aggregate
import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.LocationId
import com.github.caay2000.ttk.shared.WorldId
import com.github.caay2000.ttk.shared.randomDomainId

data class World(
    override val id: WorldId = randomDomainId(),
    val currentTurn: Int,
    val cells: Map<Position, Cell>,
    val companies: Map<CompanyId, Company>
) : Aggregate() {

    companion object {
        fun create(width: Int, height: Int) = World(
            currentTurn = 0,
            cells = createCells(width, height),
            companies = emptyMap()
        )

        private fun createCells(width: Int, height: Int): Map<Position, Cell> {
            val cells = mutableMapOf<Position, Cell>()
            for (x in 0 until width) {
                for (y in 0 until height) {
                    cells[Position(x, y)] = Cell(x, y)
                }
            }
            return cells
        }
    }

    fun getCell(position: Position): Cell = cells.getValue(position)

    fun update(): World = copy(currentTurn = currentTurn + 1)

    fun createConnection(path: Collection<Cell>): World =
        path.fold(initial = this) { world, cell -> world.updateCell { cell } }

    fun addLocation(locationId: LocationId, position: Position): World =
        updateCell { getCell(position).updateLocationId(locationId) }

    private fun updateCell(cell: () -> Cell): World = cell().let { newCell ->
        copy(cells = cells + (newCell.position to newCell))
    }
}
