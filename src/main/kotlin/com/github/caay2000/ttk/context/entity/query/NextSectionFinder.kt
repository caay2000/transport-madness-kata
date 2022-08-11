package com.github.caay2000.ttk.context.entity.query

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.pathfinding.AStartPathfindingStrategy
import com.github.caay2000.ttk.pathfinding.PathfindingConfiguration

class NextSectionFinder(private val provider: Provider) {

    private val pathfindingStrategy = AStartPathfindingStrategy(PathfindingConfiguration())

    fun invoke(source: Position, target: Position): List<Cell> =
        provider.get().bind().let { world ->
            pathfindingStrategy.invoke(
                cells = world.connectedCells,
                source = world.getCell(source),
                target = world.getCell(target)
            ).map { result -> result.removeFirstCell().path }.bind()
        }
}
