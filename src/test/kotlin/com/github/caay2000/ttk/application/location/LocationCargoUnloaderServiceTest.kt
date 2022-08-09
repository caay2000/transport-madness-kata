package com.github.caay2000.ttk.application.location

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.infra.eventbus.event.Event
import com.github.caay2000.ttk.infra.eventbus.event.EventPublisher
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mock.EventPublisherMock
import com.github.caay2000.ttk.mother.WorldMother
import com.github.caay2000.ttk.mother.world.location.LocationMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class LocationCargoUnloaderServiceTest {

    private val provider = DefaultProvider()
    private val eventPublisher: EventPublisher<Event> = EventPublisherMock()
    private val sut = LocationCargoUnloaderService(provider, eventPublisher)

    @Test
    fun `should remove the amount passengers from station`() {

        val world = WorldMother.oneLocation(LocationMother.random(rawPAX = 20.0))
        provider.set(world)
        val location = world.locations.values.first()

        sut.invoke(location.position, 10).shouldBeRight {
            assertThat(it.received).isEqualTo(10)
            assertThat(it).isEqualTo(provider.get().bind().getLocation(location.id))
        }
    }
}
