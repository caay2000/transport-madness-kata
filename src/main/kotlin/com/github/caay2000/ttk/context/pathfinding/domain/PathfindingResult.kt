package com.github.caay2000.ttk.context.pathfinding.domain

import com.github.caay2000.ttk.context.world.domain.Cell

data class PathfindingResult(
    val path: List<Cell>,
    val cost: Int
) {

    fun removeFirstCell(): PathfindingResult = copy(path = path.drop(1))
}
