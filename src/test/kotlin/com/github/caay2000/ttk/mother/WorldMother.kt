package com.github.caay2000.ttk.mother

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.pathfinding.domain.AStartPathfindingStrategy
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.mother.entity.pathfinding.PathfindingConfigurationMother
import com.github.caay2000.ttk.mother.world.CellMother
import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.WorldId
import com.github.caay2000.ttk.shared.randomDomainId

object WorldMother {

    private val pathfindingStrategy = AStartPathfindingStrategy()
    private val pathfindingConfiguration = PathfindingConfigurationMother.default(needConnection = false)

    private const val DEFAULT_WIDTH: Int = 6
    private const val DEFAULT_HEIGHT: Int = 6

    fun empty(
        id: WorldId = randomDomainId(),
        width: Int = DEFAULT_WIDTH,
        height: Int = DEFAULT_HEIGHT,
        companies: Map<CompanyId, Company> = emptyMap()
    ): World = World(
        id = id,
        currentTurn = 0,
        cells = createCells(width, height),
        companies = companies
    )

    fun random(
        width: Int = DEFAULT_WIDTH,
        height: Int = DEFAULT_HEIGHT,
        connectedPaths: Map<Position, List<Position>> = emptyMap()
    ): World = empty(width = width, height = height).connectPath(connectedPaths)

    private fun createCells(width: Int, height: Int): Map<Position, Cell> {
        val cells = mutableMapOf<Position, Cell>()
        for (x in 0 until width) {
            for (y in 0 until height) {
                cells[Position(x, y)] = Cell(x, y, Cell.NotConnetedCell)
            }
        }
        return cells
    }

    private fun World.connectPath(paths: Map<Position, List<Position>>, companyId: CompanyId = randomDomainId()): World {
        val updatedCells: MutableMap<Position, Cell> = mutableMapOf()
        this.cells.toMap(updatedCells)
        paths.forEach { (source, targets) ->
            targets.forEach { target ->
                val path = pathfindingStrategy.invoke(pathfindingConfiguration, this.cells.values.toSet(), this.getCell(source), this.getCell(target)).bind().path
                path.forEach { cell ->
                    updatedCells[cell.position] = CellMother.random(cell.position.x, cell.position.y, Cell.ConnectedCell(companyId))
                }
            }
        }
        return this.copy(cells = updatedCells)
    }
}
