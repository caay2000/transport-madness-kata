package com.github.caay2000.ttk.application.entity

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.domain.entity.Route
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.infra.eventbus.event.Event
import com.github.caay2000.ttk.infra.eventbus.event.EventPublisher
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mock.EventPublisherMock
import com.github.caay2000.ttk.mother.EntityMother
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeLeftOfType
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EntityRouteAssignerServiceTest {

    private val provider = DefaultProvider()
    private val eventPublisher: EventPublisher<Event> = EventPublisherMock()
    private val sut = EntityRouteAssignerService(provider, eventPublisher)

    @Test
    fun `route is added to entity`() {

        val entity = EntityMother.random()
        val world = WorldMother.oneVehicle(entity = entity)
        provider.set(world)

        val stops = listOf(Position(1, 1), Position(3, 2))
        sut.invoke(entity.id, stops).shouldBeRight {
            assertThat(it).isEqualTo(entity.copy(route = Route(stops)))
            assertThat(it).isEqualTo(provider.get().bind().entities.values.first())
        }
    }

    @Test
    fun `should fail if route is invalid`() {

        val entity = EntityMother.random()
        val world = WorldMother.oneVehicle(entity = entity)
        provider.set(world)

        sut.invoke(entity.id, emptyList()).shouldBeLeftOfType<InvalidRouteException>()
    }
}
