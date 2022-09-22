package com.github.caay2000.ttk.context.pathfinding.domain

import arrow.core.Either
import com.github.caay2000.ttk.context.world.domain.Cell

interface PathfindingStrategy {

    fun invoke(pathfindingConfiguration: PathfindingConfiguration, cells: Collection<Cell>, source: Cell, target: Cell): Either<PathfindingException, PathfindingResult>
}
