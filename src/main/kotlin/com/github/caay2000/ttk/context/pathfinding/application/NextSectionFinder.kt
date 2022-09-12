package com.github.caay2000.ttk.context.pathfinding.application

import arrow.core.Either
import com.github.caay2000.ttk.context.pathfinding.domain.AStartPathfindingStrategy
import com.github.caay2000.ttk.context.pathfinding.domain.PathfindingConfiguration
import com.github.caay2000.ttk.context.pathfinding.domain.PathfindingException
import com.github.caay2000.ttk.context.pathfinding.domain.PathfindingStrategy
import com.github.caay2000.ttk.context.pathfinding.domain.Section
import com.github.caay2000.ttk.context.world.domain.Cell

class NextSectionFinder(pathfindingConfiguration: PathfindingConfiguration) {

    private val pathfindingStrategy: PathfindingStrategy = AStartPathfindingStrategy(pathfindingConfiguration)

    fun invoke(cells: List<Cell>, source: Cell, target: Cell): Either<PathfindingException, Section> =
        pathfindingStrategy.invoke(cells, source, target)
            .map { result -> Section(result.removeFirstCell().path) }
}
