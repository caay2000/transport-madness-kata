package com.github.caay2000.ttk.application.pathfinding

import arrow.core.Either
import com.github.caay2000.ttk.domain.world.Cell

interface PathfindingStrategy {

    val pathfindingConfiguration: PathfindingConfiguration

    fun invoke(cells: Set<Cell>, source: Cell, target: Cell): Either<PathfindingException, PathfindingResult>
}
