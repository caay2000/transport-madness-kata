package com.github.caay2000.ttk.context.location.domain

import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.shared.Aggregate
import com.github.caay2000.ttk.shared.LocationId
import com.github.caay2000.ttk.shared.randomDomainId
import kotlin.math.floor

data class Location(
    override val id: LocationId,
    val position: Position,
    val population: Int,
    val rawPAX: Double = 0.0,
    val received: Int = 0,
    @Transient
    val configuration: Configuration
) : Aggregate() {

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
    fun unload(amountUnloaded: Int): Location = copy(received = received + amountUnloaded)
    fun load(amountLoaded: Int): Location = copy(rawPAX = rawPAX - amountLoaded)
}
