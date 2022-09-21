package com.github.caay2000.ttk.mother.world.location

import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.mother.StringMother
import com.github.caay2000.ttk.mother.world.PositionMother

object LocationMother {

    fun random(
        name: String = StringMother.random(),
        position: Position = PositionMother.random(),
        population: Int = PopulationMother.random(),
        rawPAX: Double = 0.0
    ) = Location.create(
        name = name,
        position = position,
        population = population,
        rawPAX = rawPAX
    )
}
