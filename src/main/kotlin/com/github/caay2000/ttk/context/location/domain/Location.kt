package com.github.caay2000.ttk.context.location.domain

import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.shared.Aggregate
import com.github.caay2000.ttk.shared.LocationId
import com.github.caay2000.ttk.shared.randomDomainId
import kotlin.math.floor

data class Location(
    override val id: LocationId,
    val name: String,
    val position: Position,
    val population: Int,
    val rawPAX: Double = 0.0,
    val received: Int = 0
) : Aggregate() {

    companion object {
        fun create(name: String, position: Position, population: Int, rawPAX: Double = 0.0) = Location(
            id = randomDomainId(),
            name = name,
            position = position,
            population = population,
            rawPAX = rawPAX
        )
    }

    val configuration: LocationConfiguration
        get() = LocationConfiguration.get()

    val pax: Int
        get() = floor(rawPAX).toInt()

    fun update(): Location = copy(rawPAX = rawPAX + population * configuration.cityPAXPercentage)
    fun unload(amountUnloaded: Int): Location = copy(received = received + amountUnloaded)
    fun load(amountLoaded: Int): Location = copy(rawPAX = rawPAX - amountLoaded)
}
