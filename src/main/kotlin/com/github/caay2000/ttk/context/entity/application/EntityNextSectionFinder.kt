package com.github.caay2000.ttk.context.entity.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.pathfinding.AStartPathfindingStrategy
import com.github.caay2000.ttk.pathfinding.PathfindingConfiguration

class EntityNextSectionFinder(private val provider: Provider) {

    private val pathfindingStrategy = AStartPathfindingStrategy(PathfindingConfiguration())

    fun invoke(source: Position, target: Position): Either<EntityException, List<Cell>> =
        findWorld()
            .flatMap { world -> world.findNextSection(source, target) }

    fun findWorld(): Either<EntityException, World> =
        provider.get()
            .mapLeft { UnknownEntityException(it) }

    private fun World.findNextSection(source: Position, target: Position): Either<EntityException, List<Cell>> =
        pathfindingStrategy.invoke(cells = this.connectedCells, source = this.getCell(source), target = this.getCell(target))
            .map { result -> result.removeFirstCell().path }
            .mapLeft { InvalidEntityNextSectionException(source, target, it) }
}
