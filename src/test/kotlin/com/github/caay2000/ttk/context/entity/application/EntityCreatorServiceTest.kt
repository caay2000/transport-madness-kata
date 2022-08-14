package com.github.caay2000.ttk.context.entity.application

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.domain.InvalidEntityPositionException
import com.github.caay2000.ttk.context.entity.domain.PassengerTrain
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mother.ConfigurationMother
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeLeftOfType
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.kotlin.mock

internal class EntityCreatorServiceTest {

    private val provider = DefaultProvider()
    private val eventPublisher: EventPublisher<Event> = mock()
    private val sut = EntityCreatorService(provider, eventPublisher)

    @Test
    fun `entity is added to world correctly`() {

        provider.set(WorldMother.empty())
        provider.setConfiguration(ConfigurationMother.random())

        sut.invoke(PassengerTrain(3), Position(1, 1)).shouldBeRight {
            assertThat(provider.get().bind().entities).hasSize(1)
            assertThat(it).isEqualTo(provider.get().bind().entities.values.first())
        }
    }

    @ParameterizedTest
    @CsvSource(value = ["-1,0", "0,-1", "1,0", "0,1"])
    fun `entity added out of bounds throw exception`(x: Int, y: Int) {
        provider.set(WorldMother.empty(width = 1, height = 1))

        sut.invoke(PassengerTrain(), Position(x, y)).shouldBeLeftOfType<InvalidEntityPositionException>()
    }
}
