package com.github.caay2000.ttk.pathfinding

import arrow.core.Either
import com.github.caay2000.ttk.context.world.domain.Cell

interface PathfindingStrategy {

    val pathfindingConfiguration: PathfindingConfiguration

    fun invoke(cells: Set<Cell>, source: Cell, target: Cell): Either<PathfindingException, PathfindingResult>
}
