package com.github.caay2000.ttk.context.world.domain

import kotlin.math.sqrt

data class Position(val x: Int, val y: Int) {

    override fun toString(): String = "($x,$y)"

    fun distance(destination: Position): Float {
        val x = destination.x - x
        val y = destination.y - y
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }

    infix fun distanceTo(a: Position) = this.distance(a)

    private fun sum(position: Position): Position = copy(x = x + position.x, y = y + position.y)

    val evenRow: Boolean
        get() = x.mod(2) == 0

    val neighbours: Set<Position>
        get() = when {
            evenRow -> evenRowNeighbours
            else -> oddRowNeighbours
        }

    private val evenRowNeighbours: Set<Position>
        get() = setOf(
            this.sum(Position(1, 0)),
            this.sum(Position(0, -1)),
            this.sum(Position(-1, -1)),
            this.sum(Position(-1, 0)),
            this.sum(Position(-1, 1)),
            this.sum(Position(0, 1))
        )

    private val oddRowNeighbours: Set<Position>
        get() = setOf(
            this.sum(Position(1, 0)),
            this.sum(Position(1, -1)),
            this.sum(Position(0, -1)),
            this.sum(Position(-1, 0)),
            this.sum(Position(0, 1)),
            this.sum(Position(1, 1))
        )

}
