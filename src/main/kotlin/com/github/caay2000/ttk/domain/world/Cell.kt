package com.github.caay2000.ttk.domain.world

data class Cell(
    val position: Position,
    val cost: Int
) {
    constructor(x: Int, y: Int, cost: Int = 0) : this(position = Position(x, y), cost)

    fun distance(target: Cell): Float = this.position.distance(target.position)
    fun samePosition(cell: Cell): Boolean = this.position == cell.position
    fun createConnection(): Cell = copy(cost = 1)

    val connected: Boolean
        get() = cost > 0
}
