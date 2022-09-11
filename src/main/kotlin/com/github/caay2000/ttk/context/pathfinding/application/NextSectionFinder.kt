package com.github.caay2000.ttk.context.pathfinding.application

import arrow.core.Either
import arrow.core.right
import com.github.caay2000.ttk.context.pathfinding.domain.Section
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.pathfinding.PathfindingConfiguration
import com.github.caay2000.ttk.pathfinding.PathfindingException

class NextSectionFinder(val pathfindingConfiguration: PathfindingConfiguration) {

    fun invoke(cells: List<Cell>, source: Cell, target: Cell): Either<PathfindingException, Section> =
        Section(emptyList()).right()
}
