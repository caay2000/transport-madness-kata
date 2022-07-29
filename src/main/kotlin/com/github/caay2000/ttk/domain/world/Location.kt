package com.github.caay2000.ttk.domain.world

import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.shared.LocationId
import com.github.caay2000.ttk.shared.randomDomainId
import kotlin.math.floor

data class Location(
    val id: LocationId,
    val position: Position,
    val population: Int,
    val rawPAX: Double = 0.0,
    @Transient
    val configuration: Configuration
) {

    val pax: Int
        get() = floor(rawPAX).toInt()

    companion object {
        fun create(position: Position, population: Int, configuration: Configuration) = Location(
            id = randomDomainId(),
            position = position,
            population = population,
            configuration = configuration
        )
    }

    fun update(): Location = copy(rawPAX = rawPAX + population * configuration.cityPAXPercentage)
}
