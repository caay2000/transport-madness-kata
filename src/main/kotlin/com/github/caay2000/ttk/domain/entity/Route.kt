package com.github.caay2000.ttk.domain.entity

import com.github.caay2000.ttk.application.entity.InvalidRouteException
import com.github.caay2000.ttk.domain.world.Cell
import com.github.caay2000.ttk.domain.world.Position

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

    fun nextStop(): Route = copy(stopIndex = nextIndex)

    fun dropNextSection(): Route =
        copy(nextSectionList = nextSectionList.drop(1))

    fun updateNextSection(updatedNextSection: List<Cell>): Route =
        copy(nextSectionList = updatedNextSection)

    private val nextIndex: Int
        get() = if (stopIndex + 1 < stops.size) stopIndex + 1 else 0
}
