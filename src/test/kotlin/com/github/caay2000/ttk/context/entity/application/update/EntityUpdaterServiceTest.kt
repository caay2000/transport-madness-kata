package com.github.caay2000.ttk.context.entity.application.update

import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityStatus
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mother.ConfigurationMother
import com.github.caay2000.ttk.mother.EntityMother
import com.github.caay2000.ttk.mother.RouteMother
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class EntityUpdaterServiceTest {

    private val provider = DefaultProvider()
    private val eventPublisher: EventPublisher<Event> = mock()
    private val sut = EntityUpdaterService(provider, eventPublisher)

    @Test
    fun `entity does not move if not needed`() {

        val entity: Entity = EntityMother.random()
        val world = WorldMother.oneVehicle(entity = entity)
        provider.set(world)

        sut.invoke(entity.id).shouldBeRight {
            assertThat(it).isEqualTo(entity.copy(currentDuration = 1))
        }
    }

    @Test
    fun `moves to first stop after configuration$turnsStoppedInStation turns in final stop`() {

        val configuration = ConfigurationMother.random()
        val route = RouteMother.random(stops = listOf(Position(3, 0), Position(3, 4)), stopIndex = 1)
        val entity: Entity = EntityMother.random(
            currentPosition = Position(3, 4),
            currentDuration = configuration.turnsStoppedInStation,
            route = route,
            status = EntityStatus.STOP,
            configuration = configuration
        )
        val world = WorldMother.connectedPaths(
            entities = mapOf(entity.id to entity),
            connectedPaths = mapOf(Position(3, 0) to listOf(Position(3, 4)))
        )
        provider.set(world)

        sut.invoke(entity.id).shouldBeRight {
            assertThat(it.currentPosition).isEqualTo(Position(3, 3))
            assertThat(it.route.stopIndex).isEqualTo(0)
            assertThat(it.status).isEqualTo(EntityStatus.IN_ROUTE)
        }
    }
}
