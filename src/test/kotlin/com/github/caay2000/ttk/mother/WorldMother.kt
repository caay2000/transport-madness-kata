package com.github.caay2000.ttk.mother

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.mother.entity.pathfinding.PathfindingConfigurationMother
import com.github.caay2000.ttk.mother.world.CellMother
import com.github.caay2000.ttk.mother.world.location.LocationMother
import com.github.caay2000.ttk.pathfinding.AStartPathfindingStrategy
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.LocationId
import com.github.caay2000.ttk.shared.WorldId
import com.github.caay2000.ttk.shared.randomDomainId

object WorldMother {

    private val pathfindingStrategy = AStartPathfindingStrategy(PathfindingConfigurationMother.default(needConnection = false))

    private const val DEFAULT_WIDTH: Int = 6
    private const val DEFAULT_HEIGHT: Int = 6

    fun empty(
        id: WorldId = randomDomainId(),
        width: Int = DEFAULT_WIDTH,
        height: Int = DEFAULT_HEIGHT,
        entities: Map<EntityId, Entity> = emptyMap(),
        locations: Map<LocationId, Location> = emptyMap()
    ): World = World(
        id = id,
        currentTurn = 0,
        cells = createCells(width, height),
        entities = entities,
        locations = emptyMap()
    ).let {
        locations.values.fold(it) { world, location -> world.addLocation(location) }
    }

    fun random(
        width: Int = DEFAULT_WIDTH,
        height: Int = DEFAULT_HEIGHT,
        entities: Map<EntityId, Entity> = emptyMap(),
        locations: Map<LocationId, Location> = emptyMap(),
        connectedPaths: Map<Position, List<Position>> = emptyMap()
    ): World = empty(width = width, height = height, entities = entities, locations = locations).connectPath(connectedPaths)

    fun oneVehicle(entity: Entity): World =
        empty(entities = mapOf(entity.id to entity))

    fun oneLocation(location: Location = LocationMother.random()): World =
        empty(locations = mapOf(location.id to location))

    private fun createCells(width: Int, height: Int): Set<Cell> {
        val cells = mutableSetOf<Cell>()
        for (x in 0 until width) {
            for (y in 0 until height) {
                cells.add(Cell(x, y, Cell.CellConnection.NOT_CONNECTED))
            }
        }
        return cells
    }

    private fun World.connectPath(paths: Map<Position, List<Position>>): World {
        val updatedCells = this.cells as MutableSet
        paths.forEach { (source, targets) ->
            targets.forEach { target ->
                val path = pathfindingStrategy.invoke(this.cells, this.getCell(source), this.getCell(target)).bind().path
                path.forEach { cell ->
                    updatedCells.removeIf { it.samePosition(cell) }
                    updatedCells.add(CellMother.random(cell.position.x, cell.position.y, Cell.CellConnection.CONNECTED))
                }
            }
        }
        return this.copy(cells = updatedCells)
    }
}
