package com.github.caay2000.ttk.application.world.create

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.application.pathfinding.AStartPathfindingStrategy
import com.github.caay2000.ttk.application.pathfinding.PathfindingConfiguration
import com.github.caay2000.ttk.application.pathfinding.PathfindingResult
import com.github.caay2000.ttk.application.world.UnknownWorldException
import com.github.caay2000.ttk.application.world.WorldException
import com.github.caay2000.ttk.application.world.WorldService
import com.github.caay2000.ttk.domain.world.Cell
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.infra.eventbus.event.Event
import com.github.caay2000.ttk.infra.eventbus.event.EventPublisher

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
        this.path.map { cell -> cell.createConnection() }.toSet()
}
