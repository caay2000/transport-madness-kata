package com.github.caay2000.ttk.context.entity.application.update

import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityStatus
import com.github.caay2000.ttk.context.entity.event.EntityUnloadedEvent
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mother.EntityMother
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

internal class EntityUpdateUnloaderServiceTest {

    private val provider = DefaultProvider()
    private val eventPublisher: EventPublisher<Event> = mock()
    private val sut = EntityUpdateUnloaderService(provider, eventPublisher)

    @Test
    fun `should unload passengers (event) when reaches a station`() {

        val entity: Entity = EntityMother.random(
            currentPosition = Position(3, 0),
            pax = 10,
            status = EntityStatus.STOP
        )
        val world = WorldMother.connectedPaths(
            entities = mapOf(entity.id to entity),
            connectedPaths = mapOf(Position(0, 0) to listOf(Position(3, 0)))
        )
        provider.set(world)

        sut.invoke(entity).shouldBeRight {
            assertThat(it.pax).isEqualTo(0)
        }
        verify(eventPublisher).publish(
            listOf(EntityUnloadedEvent(aggregateId = entity.id, amount = 10, position = Position(3, 0)))
        )
    }
}
