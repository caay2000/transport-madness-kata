package com.github.caay2000.ttk.domain.world

import kotlin.math.sqrt

data class Position(val x: Int, val y: Int) {

    fun distance(destination: Position): Float {
        val x = destination.x - x
        val y = destination.y - y
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }

    fun sum(position: Position): Position = copy(x = x + position.x, y = y + position.y)
}
