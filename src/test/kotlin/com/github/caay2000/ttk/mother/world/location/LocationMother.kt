package com.github.caay2000.ttk.mother.world.location

import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.mother.ConfigurationMother
import com.github.caay2000.ttk.mother.world.PositionMother
import com.github.caay2000.ttk.shared.LocationId
import com.github.caay2000.ttk.shared.randomDomainId

object LocationMother {

    fun random(
        id: LocationId = randomDomainId(),
        position: Position = PositionMother.random(),
        population: Int = PopulationMother.random(),
        rawPAX: Double = 0.0,
        configuration: Configuration = ConfigurationMother.random()
    ) = Location(
        id = id,
        position = position,
        population = population,
        rawPAX = rawPAX,
        configuration = configuration
    )
}
