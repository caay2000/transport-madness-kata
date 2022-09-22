package com.github.caay2000.ttk.context.pathfinding.application

import arrow.core.Either
import com.github.caay2000.ttk.context.pathfinding.domain.AStartPathfindingStrategy
import com.github.caay2000.ttk.context.pathfinding.domain.PathfindingConfiguration
import com.github.caay2000.ttk.context.pathfinding.domain.PathfindingException
import com.github.caay2000.ttk.context.pathfinding.domain.PathfindingStrategy
import com.github.caay2000.ttk.context.pathfinding.domain.Section
import com.github.caay2000.ttk.context.world.domain.Cell

class PathFinder {

    private val pathfindingStrategy: PathfindingStrategy = AStartPathfindingStrategy()

    fun invoke(pathfindingConfiguration: PathfindingConfiguration, cells: Collection<Cell>, source: Cell, target: Cell): Either<PathfindingException, Section> =
        pathfindingStrategy.invoke(pathfindingConfiguration, cells, source, target)
            .map { result -> Section(result.path) }
}
