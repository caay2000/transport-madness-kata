package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.UnknownWorldException
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.WorldException
import com.github.caay2000.ttk.context.world.domain.WorldNotFoundWorldException
import com.github.caay2000.ttk.pathfinding.AStartPathfindingStrategy
import com.github.caay2000.ttk.pathfinding.PathfindingConfiguration
import com.github.caay2000.ttk.pathfinding.PathfindingResult

class WorldConnectionCreatorService(
    private val worldRepository: WorldRepository,
    private val eventPublisher: EventPublisher
) {

    private val pathfinding by lazy { AStartPathfindingStrategy(PathfindingConfiguration.getCreteConnectionStrategyConfiguration()) }

    fun invoke(source: Position, target: Position): Either<WorldException, World> =
        findWorld()
            .flatMap { world -> world.createConnection(source, target) }
            .flatMap { world -> world.save() }
            .flatMap { world -> world.publishEvents() }

    private fun World.createConnection(source: Position, target: Position): Either<WorldException, World> =
        pathfinding.invoke(cells.values.toSet(), getCell(source), getCell(target))
            .map { path -> path.updateCellsConnection() }
            .map { updatedCells -> createConnection(updatedCells) }
            .mapLeft { error -> UnknownWorldException(error) }

    private fun PathfindingResult.updateCellsConnection(): Set<Cell> =
        this.path.map { cell -> cell.createConnection() }.toSet()

    private fun findWorld(): Either<WorldException, World> =
        worldRepository.get()
            .mapLeft { WorldNotFoundWorldException(it) }

    private fun World.save(): Either<WorldException, World> =
        worldRepository.save(this)
            .mapLeft { UnknownWorldException(it) }

    private fun World.publishEvents(): Either<WorldException, World> =
        Either.catch { eventPublisher.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownWorldException(it) }
}
