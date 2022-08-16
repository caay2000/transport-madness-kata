package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.UnknownWorldException
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.WorldException
import com.github.caay2000.ttk.pathfinding.AStartPathfindingStrategy
import com.github.caay2000.ttk.pathfinding.PathfindingConfiguration
import com.github.caay2000.ttk.pathfinding.PathfindingResult
import com.github.caay2000.ttk.shared.mapToSet

class WorldConnectionCreatorService(
    provider: Provider,
    eventPublisher: EventPublisher<Event>,
    pathfindingConfiguration: PathfindingConfiguration
) : WorldService(provider, eventPublisher) {

    private val pathfinding = AStartPathfindingStrategy(pathfindingConfiguration)

    fun invoke(source: Position, target: Position): Either<WorldException, World> =
        findWorld()
            .flatMap { world -> world.createConnection(source, target) }
            .flatMap { world -> world.save() }
            .flatMap { world -> world.publishEvents() }

    private fun World.createConnection(source: Position, target: Position): Either<WorldException, World> =
        pathfinding.invoke(cells, getCell(source), getCell(target))
            .map { path -> path.updateCellsConnection() }
            .map { updatedCells -> createConnection(updatedCells) }
            .mapLeft { error -> UnknownWorldException(error) }

    private fun PathfindingResult.updateCellsConnection(): Set<Cell> =
        this.path.mapToSet { cell -> cell.createConnection() }
}
