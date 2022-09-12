package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.pathfinding.domain.AStartPathfindingStrategy
import com.github.caay2000.ttk.context.pathfinding.domain.PathfindingConfiguration
import com.github.caay2000.ttk.context.pathfinding.domain.PathfindingResult
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.UnknownWorldException
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.WorldException

class WorldConnectionCreatorService(worldRepository: WorldRepository, eventPublisher: EventPublisher) {

    private val worldService: WorldServiceApi = worldService(worldRepository, eventPublisher)
    private val pathfinding by lazy { AStartPathfindingStrategy(PathfindingConfiguration.getCreteConnectionStrategyConfiguration()) }

    fun invoke(source: Position, target: Position): Either<WorldException, World> =
        worldService.find()
            .flatMap { world -> world.createConnection(source, target) }
            .flatMap { world -> worldService.save(world) }
            .flatMap { world -> worldService.publishEvents(world) }

    private fun World.createConnection(source: Position, target: Position): Either<WorldException, World> =
        pathfinding.invoke(cells.values.toSet(), getCell(source), getCell(target))
            .map { path -> path.updateCellsConnection() }
            .map { updatedCells -> createConnection(updatedCells) }
            .mapLeft { error -> UnknownWorldException(error) }

    private fun PathfindingResult.updateCellsConnection(): Set<Cell> =
        this.path.map { cell -> cell.createConnection() }.toSet()
}
