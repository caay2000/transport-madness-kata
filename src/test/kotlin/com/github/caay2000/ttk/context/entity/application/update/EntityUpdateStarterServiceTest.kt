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
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class EntityUpdateStarterServiceTest {

    private val provider = DefaultProvider()
    private val eventPublisher: EventPublisher<Event> = mock()
    private val sut = EntityUpdateStarterService(provider, eventPublisher)

    @Test
    fun `entity should wait x turns in STOP`() {

        val entity: Entity = EntityMother.random(
            currentPosition = Position(3, 0),
            currentDuration = 1,
            route = RouteMother.random(Position(3, 0), Position(2, 4)),
            status = EntityStatus.STOP

        )
        val world = WorldMother.oneVehicle(entity = entity)
        provider.set(world)

        sut.invoke(entity).shouldBeRight {
            Assertions.assertThat(it).isEqualTo(entity)
        }
    }

    @Test
    fun `entity starts movement to next route destination after configuration$turnsStoppedInStation turns in STOP`() {

        val configuration = ConfigurationMother.random()
        val route = RouteMother.random(stops = listOf(Position(3, 0), Position(3, 4)), stopIndex = 0)
        val entity: Entity = EntityMother.random(
            currentPosition = Position(3, 0),
            currentDuration = configuration.turnsStoppedInStation + 1,
            route = route,
            status = EntityStatus.STOP,
            configuration = configuration
        )
        val world = WorldMother.connectedPaths(
            entities = mapOf(entity.id to entity),
            connectedPaths = mapOf(Position(3, 0) to listOf(Position(3, 4)))
        )
        provider.set(world)

        sut.invoke(entity).shouldBeRight {
            Assertions.assertThat(it).isEqualTo(
                entity.copy(
                    status = EntityStatus.IN_ROUTE,
                    route = entity.route.nextStop(),
                    currentDuration = 0
                )
            )
        }
    }
}
