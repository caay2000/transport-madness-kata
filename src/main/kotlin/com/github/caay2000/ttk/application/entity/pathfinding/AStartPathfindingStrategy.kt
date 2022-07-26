package com.github.caay2000.ttk.application.entity.pathfinding

import com.github.caay2000.ttk.application.entity.pathfinding.PathfindingStrategy.Node
import com.github.caay2000.ttk.domain.world.Cell
import com.github.caay2000.ttk.domain.world.Position

class AStartPathfindingStrategy : PathfindingStrategy {

    override fun invoke(cells: Set<Cell>, source: Cell, target: Cell): Path =
        recursiveInvoke(
            cells = Grid(cells.associate { it.position to Node(it, 1) }),
            source = source,
            target = target
        )

    private fun recursiveInvoke(cells: Grid, source: Cell, target: Cell): Path {

        val openVertices = mutableSetOf(source)
        val closedVertices = mutableSetOf<Cell>()
        val costFromStart = mutableMapOf(source to 0)
        val estimatedTotalCost = mutableMapOf(source to source.distance(target))
        val cameFrom = mutableMapOf<Cell, Cell>()

        while (openVertices.size > 0) {
            val currentPos = openVertices.minBy { estimatedTotalCost.getValue(it) }
            if (currentPos == target) {
                val path = generatePath(currentPos, cameFrom)
                val cost = estimatedTotalCost.getValue(target)
                return Path(path, cost)
            }
            openVertices.remove(currentPos)
            closedVertices.add(currentPos)
            cells.getNeighbours(currentPos)
                .filterNot { closedVertices.contains(it) }
                .forEach { neighbour ->
                    val score = costFromStart.getValue(currentPos) + cells.moveCost(currentPos, neighbour)
                    if (score < costFromStart.getOrDefault(neighbour, MAX_SCORE)) {
                        if (!openVertices.contains(neighbour)) {
                            openVertices.add(neighbour)
                        }
                        cameFrom[neighbour] = currentPos
                        costFromStart[neighbour] = score
                        estimatedTotalCost[neighbour] = score + neighbour.distance(target)
                    }
                }
        }
        throw IllegalArgumentException("No Path from Start $source to Finish $target")
    }

    private fun generatePath(currentPos: Cell, cameFrom: Map<Cell, Cell>): Set<Cell> {
        val path = mutableListOf(currentPos)
        var current = currentPos
        while (cameFrom.containsKey(current)) {
            current = cameFrom.getValue(current)
            path.add(0, current)
        }
        return path.toSet()
    }

    data class Grid(val cells: Map<Position, Node>) {

        fun getNeighbours(position: Cell): Set<Cell> =
            position.position.neighbours
                .filter { cells.containsKey(it) }
                .map { cells.getValue(it).cell }
                .toSet()

        fun moveCost(currentPos: Cell, neighbour: Cell): Int {
            return currentPos.cost + neighbour.cost
        }
    }

    companion object {
        private const val MAX_SCORE = 99999999
    }
}
