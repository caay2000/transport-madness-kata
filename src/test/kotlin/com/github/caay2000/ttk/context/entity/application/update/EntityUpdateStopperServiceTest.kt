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

internal class EntityUpdateStopperServiceTest {

    private val provider = DefaultProvider()
    private val eventPublisher: EventPublisher<Event> = mock()
    private val sut = EntityUpdateStopperService(provider, eventPublisher)

    @Test
    fun `entity should update status to STOP when reaches a stop`() {

        val entity: Entity = EntityMother.random(
            currentPosition = Position(3, 0),
            route = RouteMother.random(Position(3, 0)),
            status = EntityStatus.IN_ROUTE
        )
        val world = WorldMother.connectedPaths(
            entities = mapOf(entity.id to entity),
            connectedPaths = mapOf(Position(0, 0) to listOf(Position(3, 0)))
        )
        provider.set(world)

        sut.invoke(entity).shouldBeRight {
            Assertions.assertThat(it.currentPosition).isEqualTo(Position(3, 0))
            Assertions.assertThat(it.status).isEqualTo(EntityStatus.STOP)
        }
    }
}
