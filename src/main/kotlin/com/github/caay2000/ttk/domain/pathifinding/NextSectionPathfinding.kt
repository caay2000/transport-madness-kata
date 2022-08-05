package com.github.caay2000.ttk.domain.pathifinding

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.application.pathfinding.AStartPathfindingStrategy
import com.github.caay2000.ttk.application.pathfinding.PathfindingConfiguration
import com.github.caay2000.ttk.domain.world.Cell
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.Provider

typealias NextSectionPathfinding = (Position, Position) -> List<Cell>

class NextSectionFinder(private val provider: Provider) : NextSectionPathfinding {

    private val pathfindingStrategy = AStartPathfindingStrategy(PathfindingConfiguration())

    override fun invoke(source: Position, target: Position): List<Cell> =
        provider.get().bind().let { world ->
            pathfindingStrategy.invoke(
                cells = world.connectedCells,
                source = world.getCell(source),
                target = world.getCell(target)
            ).map { result -> result.removeFirstCell().path }.bind()
        }
}
