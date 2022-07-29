package com.github.caay2000.ttk.application.world.location

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.application.world.LocationsTooCloseException
import com.github.caay2000.ttk.domain.world.Location
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mother.ConfigurationMother
import com.github.caay2000.ttk.mother.WorldMother
import com.github.caay2000.ttk.mother.world.city.PopulationMother
import io.kotest.assertions.arrow.either.shouldBeLeftOfType
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LocationCreatorServiceTest {

    private val provider: Provider = DefaultProvider()

    private val sut = LocationCreatorService(provider)

    @Test
    fun `city is created correctly`() {

        provider.set(WorldMother.empty())
        provider.setConfiguration(ConfigurationMother.random())

        val position = Position(0, 0)
        val population = PopulationMother.random()

        sut.invoke(position, population).shouldBeRight {
            val locationId = it.getCell(position).locationId!!
            assertThat(it.locations).hasSize(1)
            assertThat(locationId).isNotNull
            assertThat(it.locations[locationId]).isEqualTo(Location(locationId, position, population))
            assertThat(it).isEqualTo(provider.get().bind())
        }
    }

    @Test
    fun `city creation fails if distance between cities is less than configuration$minDistanceBetweenCities`() {
        provider.set(WorldMother.empty())
        provider.setConfiguration(ConfigurationMother.random())

        sut.invoke(Position(0, 0), PopulationMother.random()).shouldBeRight()
        sut.invoke(Position(0, 2), PopulationMother.random()).shouldBeLeftOfType<LocationsTooCloseException>()
    }
}
