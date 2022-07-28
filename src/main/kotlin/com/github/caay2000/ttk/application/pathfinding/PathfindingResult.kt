package com.github.caay2000.ttk.application.pathfinding

import com.github.caay2000.ttk.domain.world.Cell

data class PathfindingResult(
    val path: Set<Cell>,
    val cost: Float
)
