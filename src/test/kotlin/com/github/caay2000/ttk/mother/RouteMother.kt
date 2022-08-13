package com.github.caay2000.ttk.mother

import com.github.caay2000.ttk.context.entity.domain.Route
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position

object RouteMother {

    fun random(
        stops: List<Position>,
        stopIndex: Int = 0,
        nextSectionList: List<Cell> = emptyList()
    ) = Route(
        stops = stops,
        stopIndex = stopIndex,
        nextSectionList = nextSectionList
    )
}
