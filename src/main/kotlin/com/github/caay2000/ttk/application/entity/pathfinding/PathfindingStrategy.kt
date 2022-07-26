package com.github.caay2000.ttk.application.entity.pathfinding

import com.github.caay2000.ttk.domain.world.Cell

interface PathfindingStrategy {

    fun invoke(cells: Set<Cell>, source: Cell, target: Cell): Path

    data class Node(val cell: Cell, val cost: Int)
}
