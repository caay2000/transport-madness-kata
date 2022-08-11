package com.github.caay2000.ttk.pathfinding

import arrow.core.Either
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position

class AStartPathfindingStrategy(override val pathfindingConfiguration: PathfindingConfiguration) : PathfindingStrategy {

    override fun invoke(cells: Set<Cell>, source: Cell, target: Cell): Either<PathfindingException, PathfindingResult> =
        Either.catch {
            invoke(
                grid = createGrid(cells),
                source = source,
                target = target
            )
        }.mapLeft { UnknownPathfindingException(it) }

    private fun createGrid(cells: Set<Cell>): Grid = Grid(
        cells.filter { if (pathfindingConfiguration.needConnection) it.connected else true }
            .associateBy { it.position }
    )

    private fun invoke(grid: Grid, source: Cell, target: Cell): PathfindingResult {

        val openVertices = mutableSetOf(source)
        val visitedVertices = mutableSetOf<Cell>()
        val costFromStart = mutableMapOf(source to 0)
        val estimatedTotalCost = mutableMapOf(source to source.distance(target))
        val cameFrom = mutableMapOf<Cell, Cell>()

        while (openVertices.size > 0) {
            val currentPos = openVertices.minBy { estimatedTotalCost.getValue(it) }
            if (currentPos.samePosition(target)) {
                val path = generatePath(currentPos, cameFrom)
                val cost = estimatedTotalCost.getValue(target)
                return PathfindingResult(path, cost)
            }
            openVertices.remove(currentPos)
            visitedVertices.add(currentPos)
            grid.getNeighbours(currentPos)
                .filterNot { visitedVertices.contains(it) }
                .forEach { neighbour ->
                    val score = costFromStart.getValue(currentPos) + grid.moveCost(currentPos, neighbour)
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

    private fun generatePath(currentPos: Cell, cameFrom: Map<Cell, Cell>): List<Cell> {
        val path = mutableListOf(currentPos)
        var current = currentPos
        while (cameFrom.containsKey(current)) {
            current = cameFrom.getValue(current)
            path.add(0, current)
        }
        return path
    }

    data class Grid(val cells: Map<Position, Cell>) {

        fun getNeighbours(cell: Cell): Set<Cell> =
            cell.position.neighbours
                .filter { cells.containsKey(it) }
                .map { cells.getValue(it) }
                .toSet()

        fun moveCost(currentPos: Cell, neighbour: Cell): Int {
            return currentPos.cost + neighbour.cost
        }
    }

    companion object {
        private const val MAX_SCORE = Int.MAX_VALUE
    }
}
