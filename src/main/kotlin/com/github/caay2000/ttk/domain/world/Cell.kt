package com.github.caay2000.ttk.domain.world

data class Cell(
    val position: Position,
    val cost: Int = 1
) {
    constructor(x: Int, y: Int) : this(position = Position(x, y), cost = 1)

    fun distance(target: Cell): Float = this.position.distance(target.position)

    infix fun distanceTo(a: Cell) = this.distance(a)
}
