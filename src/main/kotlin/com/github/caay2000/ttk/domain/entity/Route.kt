package com.github.caay2000.ttk.domain.entity

import com.github.caay2000.ttk.application.entity.InvalidRouteException
import com.github.caay2000.ttk.domain.world.Position

data class Route(
    val stops: List<Position>,
    val currentDestination: Position = stops.first(),
    val stopTime: Int = 0
) {

    companion object {
        fun create(stops: List<Position>): Route {
            if (stops.isEmpty()) throw InvalidRouteException
            return Route(stops)
        }
    }

    fun nextStop(): Route = copy(
        currentDestination = stops
            .getOrElse(stops.indexOf(currentDestination) + 1) { stops[0] }
    )
}
