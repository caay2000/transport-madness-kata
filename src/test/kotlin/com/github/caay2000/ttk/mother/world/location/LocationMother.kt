package com.github.caay2000.ttk.mother.world.location

import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.mother.ConfigurationMother
import com.github.caay2000.ttk.mother.world.PositionMother

object LocationMother {

    fun random(
        position: Position = PositionMother.random(),
        population: Int = PopulationMother.random(),
        rawPAX: Double = 0.0,
        configuration: Configuration = ConfigurationMother.random()
    ) = Location.create(
        position = position,
        population = population,
        rawPAX = rawPAX,
        configuration
    )
}
