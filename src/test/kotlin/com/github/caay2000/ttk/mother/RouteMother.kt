package com.github.caay2000.ttk.mother

import com.github.caay2000.ttk.domain.entity.Route
import com.github.caay2000.ttk.domain.world.Position

object RouteMother {

    fun random(stop: Position, vararg stops: Position) =
        Route(listOf(stop) + stops.toList())

    fun random(stops: List<Position>, stopIndex: Int = 0) =
        Route(stops = stops, stopIndex = stopIndex)
}
