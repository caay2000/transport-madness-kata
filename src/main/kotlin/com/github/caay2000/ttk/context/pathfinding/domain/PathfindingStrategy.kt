package com.github.caay2000.ttk.context.pathfinding.domain

import arrow.core.Either
import com.github.caay2000.ttk.context.world.domain.Cell

interface PathfindingStrategy {

    val pathfindingConfiguration: PathfindingConfiguration

    fun invoke(cells: Collection<Cell>, source: Cell, target: Cell): Either<PathfindingException, PathfindingResult>
}
