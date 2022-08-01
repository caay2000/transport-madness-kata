package com.github.caay2000.ttk.domain.entity

import com.github.caay2000.ttk.application.entity.InvalidRouteException
import com.github.caay2000.ttk.domain.world.Cell
import com.github.caay2000.ttk.domain.world.Position

data class Route(
    val stops: List<Position>,
    val stopIndex: Int = 0,
    val stopTime: Int = 0,
    val nextSection: List<Cell> = emptyList()
) {

    companion object {
        fun create(stops: List<Position>): Route {
            if (stops.isEmpty()) throw InvalidRouteException(stops)
            return Route(stops = stops)
        }
    }

    val currentDestination: Position
        get() = stops[stopIndex]

    val nextDestination: Position
        get() = stops[nextIndex]

    fun nextStop(): Route = copy(stopIndex = nextIndex)

    fun popNextSection(): Pair<Cell, Route> =
        nextSection.first().let { section -> section to copy(nextSection = nextSection - section) }

    private val nextIndex: Int
        get() = if (stopIndex + 1 < stops.size) stopIndex + 1 else 0
}
