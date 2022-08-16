package com.github.caay2000.ttk.context.world.domain

import kotlin.math.abs

data class Position(val x: Int, val y: Int) {

    override fun toString(): String = "($x,$y)"

    fun distance(destination: Position): Int = axialDistance(this, destination)

    private fun sum(position: Position): Position = copy(x = x + position.x, y = y + position.y)

    private val evenRow: Boolean
        get() = y.mod(2) == 0

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

    private fun axialDistance(a: Position, b: Position): Int {
        val q = a.x - (a.y - (a.y and 1)) / 2
        val q1 = b.x - (b.y - (b.y and 1)) / 2
        val pair = Pair((q - q1), (a.y - b.y))
        return (abs(pair.first) + abs(pair.first + pair.second) + abs(pair.second)) / 2
    }

    private object HexagonalOperations {

        private data class Axial(val q: Int, val r: Int)
    }

    private object HexagonalOperations222 {

        fun distance(a: Position, b: Position): Double {
            val ac = offsetToAxial(a)
            println("$a -> $ac")
            val bc = offsetToAxial(b)
            println("$b -> $bc")
            return axialDistance(ac, bc)
        }

        private data class Axial(val q: Double, val r: Double)

        private fun axialSubtract(a: Axial, b: Axial): Axial =
            Axial(a.q - b.q, a.r - b.r)

        private fun axialDistance(a: Axial, b: Axial): Double {
            var vec = axialSubtract(a, b)
            return (
                abs(vec.q) +
                    abs(vec.q + vec.r) +
                    abs(vec.r)
                ) / 2
        }

        private fun offsetToAxial(hex: Position): Axial {
            var q = hex.x - (hex.y - (hex.y.mod(2))) / 2
            var r = hex.y
            return Axial(q.toDouble(), r.toDouble())
        }
    }
}
