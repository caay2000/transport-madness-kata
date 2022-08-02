package com.github.caay2000.ttk.domain.entity

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.application.pathfinding.AStartPathfindingStrategy
import com.github.caay2000.ttk.application.pathfinding.PathfindingConfiguration
import com.github.caay2000.ttk.application.pathfinding.PathfindingResult
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.World

typealias NextSectionPathfinding = (Position, Position) -> PathfindingResult

class NextSectionFinder(private val world: World) : NextSectionPathfinding {

    private val pathfindingStrategy = AStartPathfindingStrategy(PathfindingConfiguration())

    override fun invoke(source: Position, target: Position): PathfindingResult =

        pathfindingStrategy.invoke(
            cells = world.connectedCells,
            source = world.getCell(source),
            target = world.getCell(target)
        ).map { result -> result.removeFirstCell() }.bind()
}
