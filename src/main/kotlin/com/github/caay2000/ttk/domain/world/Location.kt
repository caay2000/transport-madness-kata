package com.github.caay2000.ttk.domain.world

import com.github.caay2000.ttk.shared.LocationId
import com.github.caay2000.ttk.shared.randomDomainId

data class Location(
    val id: LocationId,
    val position: Position,
    val population: Int
) {

    companion object {
        fun create(position: Position, population: Int) = Location(randomDomainId(), position, population)
    }
}
