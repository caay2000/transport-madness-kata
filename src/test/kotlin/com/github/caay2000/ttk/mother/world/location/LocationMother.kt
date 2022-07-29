package com.github.caay2000.ttk.mother.world.location

import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.world.Location
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.mother.ConfigurationMother
import com.github.caay2000.ttk.mother.world.PositionMother
import com.github.caay2000.ttk.shared.LocationId
import com.github.caay2000.ttk.shared.randomDomainId

object LocationMother {

    fun random(
        id: LocationId = randomDomainId(),
        position: Position = PositionMother.random(),
        population: Int = PopulationMother.random(),
        configuration: Configuration = ConfigurationMother.random()
    ) = Location(
        id = id,
        position = position,
        population = population,
        configuration = configuration
    )
}
