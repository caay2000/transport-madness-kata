package com.github.caay2000.ttk.context.entity.domain

import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position

data class Route(
    val stops: List<Position>,
    val stopIndex: Int = 0,
    val stopTime: Int = 0,
    val nextSectionList: List<Cell> = emptyList()
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

    val nextSection: Cell
        get() = nextSectionList.first()

    private val nextIndex: Int
        get() = if (stopIndex + 1 < stops.size) stopIndex + 1 else 0

    fun nextStop(): Route = copy(stopIndex = nextIndex)

    fun dropNextSection(): Route =
        copy(nextSectionList = nextSectionList.drop(1))

    fun updateNextSection(updatedNextSection: List<Cell>): Route =
        copy(nextSectionList = updatedNextSection)
}
