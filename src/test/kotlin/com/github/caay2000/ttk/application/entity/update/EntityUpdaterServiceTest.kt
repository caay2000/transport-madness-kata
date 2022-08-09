package com.github.caay2000.ttk.application.entity.update

import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.entity.EntityStatus
import com.github.caay2000.ttk.domain.entity.event.EntityLoadedEvent
import com.github.caay2000.ttk.domain.entity.event.EntityUnloadedEvent
import com.github.caay2000.ttk.domain.location.Location
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mock.EventPublisherMock
import com.github.caay2000.ttk.mother.ConfigurationMother
import com.github.caay2000.ttk.mother.EntityMother
import com.github.caay2000.ttk.mother.RouteMother
import com.github.caay2000.ttk.mother.WorldMother
import com.github.caay2000.ttk.mother.world.location.LocationMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EntityUpdaterServiceTest {

    private val provider = DefaultProvider()
    private val eventPublisher: EventPublisherMock = EventPublisherMock()
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
    fun `entity moves if it has an assigned route`() {

        val entity: Entity = EntityMother.random(currentPosition = Position(0, 0), route = RouteMother.random(Position(3, 0)), currentDuration = 1)
        val world = WorldMother.connectedPaths(
            entities = mapOf(entity.id to entity),
            connectedPaths = mapOf(Position(0, 0) to listOf(Position(3, 0)))
        )
        provider.set(world)

        sut.invoke(entity.id).shouldBeRight {
            assertThat(it.currentPosition).isEqualTo(Position(1, 0))
            assertThat(it.status).isEqualTo(EntityStatus.IN_ROUTE)
        }
    }

    @Test
    fun `entity should update status to STOP when reaches a stop`() {

        val entity: Entity = EntityMother.random(
            currentPosition = Position(2, 0),
            route = RouteMother.random(Position(3, 0)),
            status = EntityStatus.IN_ROUTE
        )
        val world = WorldMother.connectedPaths(
            entities = mapOf(entity.id to entity),
            connectedPaths = mapOf(Position(0, 0) to listOf(Position(3, 0)))
        )
        provider.set(world)

        sut.invoke(entity.id).shouldBeRight {
            assertThat(it.currentPosition).isEqualTo(Position(3, 0))
            assertThat(it.status).isEqualTo(EntityStatus.STOP)
        }
    }

    @Test
    fun `entity should wait x turns in STOP`() {

        val entity: Entity = EntityMother.random(
            currentPosition = Position(3, 0),
            route = RouteMother.random(Position(3, 0), Position(2, 4)),
            status = EntityStatus.STOP
        )
        val world = WorldMother.oneVehicle(entity = entity)
        provider.set(world)

        sut.invoke(entity.id).shouldBeRight {
            assertThat(it).isEqualTo(entity.copy(currentPosition = Position(3, 0), status = EntityStatus.STOP, currentDuration = 1))
        }
    }

    @Test
    fun `entity moves to next route destination after configuration$turnsStoppedInStation turns in STOP`() {

        val configuration = ConfigurationMother.random()
        val route = RouteMother.random(stops = listOf(Position(3, 0), Position(3, 4)), stopIndex = 0)
        val entity: Entity = EntityMother.random(
            currentPosition = Position(3, 0),
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
            assertThat(it.currentPosition).isEqualTo(Position(3, 1))
            assertThat(it.route.stopIndex).isEqualTo(1)
            assertThat(it.status).isEqualTo(EntityStatus.IN_ROUTE)
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

    @Test
    fun `should unload passengers (event) when reaches a station`() {
        val entity: Entity = EntityMother.random(
            currentPosition = Position(2, 0),
            route = RouteMother.random(Position(3, 0)),
            pax = 10,
            status = EntityStatus.IN_ROUTE
        )
        val world = WorldMother.connectedPaths(
            entities = mapOf(entity.id to entity),
            connectedPaths = mapOf(Position(0, 0) to listOf(Position(3, 0)))
        )
        provider.set(world)

        sut.invoke(entity.id).shouldBeRight()
        assertThat(eventPublisher.publishedEvents)
            .hasSize(1)
            .containsExactly(EntityUnloadedEvent(aggregateId = entity.id, amount = 10, position = Position(3, 0)))
    }

    @Test
    fun `should load passengers (event) when in station for more than 1 turn`() {
        val entity: Entity = EntityMother.random(
            currentPosition = Position(3, 0),
            route = RouteMother.random(Position(3, 0), Position(2, 4)),
            pax = 10,
            status = EntityStatus.STOP
        )
        val location: Location = LocationMother.random(position = Position(3, 0), rawPAX = 20.0)
        val world = WorldMother.empty(
            entities = mapOf(entity.id to entity),
            locations = mapOf(location.id to location)
        )
        provider.set(world)

        sut.invoke(entity.id).shouldBeRight {
            assertThat(it.pax).isEqualTo(30)
        }

        assertThat(eventPublisher.publishedEvents)
            .hasSize(1)
            .containsExactly(EntityLoadedEvent(aggregateId = entity.id, amount = 20, position = Position(3, 0)))
    }
}
