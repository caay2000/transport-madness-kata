package com.github.caay2000.ttk.context.world.application

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mock.EventPublisherMock
import com.github.caay2000.ttk.mother.ConfigurationMother
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class WorldCreatorServiceTest {

    private val provider = DefaultProvider()
    private val eventPublisher: EventPublisher<Event> = EventPublisherMock()
    private val sut = WorldCreatorService(provider, eventPublisher)

    private val configuration: Configuration = ConfigurationMother.random()

    @Test
    fun `world is created correctly`() {

        provider.setConfiguration(configuration)

        sut.invoke().shouldBeRight {
            assertThat(it).isEqualTo(WorldMother.empty(id = it.id, width = configuration.worldWidth, height = configuration.worldHeight))
            assertThat(it).isEqualTo(provider.get().bind())
        }
    }
}
