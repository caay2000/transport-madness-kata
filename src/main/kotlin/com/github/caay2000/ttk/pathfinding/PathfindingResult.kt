package com.github.caay2000.ttk.pathfinding

import com.github.caay2000.ttk.context.world.domain.Cell

data class PathfindingResult(
    val path: List<Cell>,
    val cost: Float
) {

    fun removeFirstCell(): PathfindingResult = copy(path = path.drop(1))
}
