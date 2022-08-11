package com.github.caay2000.ttk.context.world.application

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mother.WorldMother
import com.github.caay2000.ttk.mother.world.location.LocationMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class WorldUpdaterServiceLocationTest {

    private val provider = DefaultProvider()
    private val sut = WorldUpdaterService(provider, mock(), mock())

    @Test
    fun `location is updated correctly with 1000 inhabitants`() {

        val location: Location = LocationMother.random(population = 1000)
        val world = WorldMother.oneLocation(location = location)
        provider.set(world)

        sut.invoke().shouldBeRight {
            assertThat(it.getLocation(location.id)).isEqualTo(location.copy(rawPAX = 2.0))
            assertThat(it.getLocation(location.id).pax).isEqualTo(2)
            assertThat(it).isEqualTo(provider.get().bind())
        }
    }

    @Test
    fun `location is updated correctly with 250 inhabitants in 2 turns`() {

        val location: Location = LocationMother.random(population = 250)
        val world = WorldMother.oneLocation(location = location)
        provider.set(world)

        sut.invoke().shouldBeRight {
            assertThat(it.getLocation(location.id)).isEqualTo(location.copy(rawPAX = 0.5))
            assertThat(it.getLocation(location.id).pax).isEqualTo(0)
            assertThat(it).isEqualTo(provider.get().bind())
        }

        sut.invoke().shouldBeRight {
            assertThat(it.getLocation(location.id)).isEqualTo(location.copy(rawPAX = 1.0))
            assertThat(it.getLocation(location.id).pax).isEqualTo(1)
            assertThat(it).isEqualTo(provider.get().bind())
        }
    }
}
