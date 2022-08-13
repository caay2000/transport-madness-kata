package com.github.caay2000.ttk.context.entity.application.update

import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityStatus
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mother.EntityMother
import com.github.caay2000.ttk.mother.RouteMother
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class EntityUpdateMoverServiceTest {

    private val provider = DefaultProvider()
    private val eventPublisher: EventPublisher<Event> = mock()
    private val sut = EntityUpdateMoverService(provider, eventPublisher)

    @Test
    fun `entity moves if it has an assigned route`() {

        val entity: Entity = EntityMother.random(
            currentPosition = Position(0, 0),
            route = RouteMother.random(Position(3, 0)),
            status = EntityStatus.IN_ROUTE,
            currentDuration = 1
        )
        val world = WorldMother.connectedPaths(
            entities = mapOf(entity.id to entity),
            connectedPaths = mapOf(Position(0, 0) to listOf(Position(3, 0)))
        )
        provider.set(world)

        sut.invoke(entity).shouldBeRight {
            Assertions.assertThat(it.currentPosition).isEqualTo(Position(1, 0))
        }
    }
}
