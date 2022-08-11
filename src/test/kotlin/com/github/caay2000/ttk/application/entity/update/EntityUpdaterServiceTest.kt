package com.github.caay2000.ttk.application.entity.update

import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.context.configuration.query.GetConfigurationQuery
import com.github.caay2000.ttk.context.configuration.query.GetConfigurationQueryHandler
import com.github.caay2000.ttk.context.entity.application.EntityUpdaterService
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityStatus
import com.github.caay2000.ttk.context.entity.event.EntityLoadedEvent
import com.github.caay2000.ttk.context.entity.event.EntityUnloadedEvent
import com.github.caay2000.ttk.context.entity.query.EntityNextSectionQuery
import com.github.caay2000.ttk.context.entity.query.EntityNextSectionQueryHandler
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.query.LocationPassengerAvailableQuery
import com.github.caay2000.ttk.context.location.query.LocationPassengerAvailableQueryHandler
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.infra.eventbus.KTEventBus
import com.github.caay2000.ttk.infra.eventbus.instantiateQueryHandler
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

    init {
        KTEventBus.init<Query, Event>()
        instantiateQueryHandler(EntityNextSectionQuery::class, EntityNextSectionQueryHandler(provider))
        instantiateQueryHandler(LocationPassengerAvailableQuery::class, LocationPassengerAvailableQueryHandler(provider))
        instantiateQueryHandler(GetConfigurationQuery::class, GetConfigurationQueryHandler(provider))
    }

    @Test
    fun `entity does not move if not needed`() {

        val entity: Entity = EntityMother.random()
        val world = WorldMother.oneVehicle(entity = entity)
        provider.set(world)
        provider.setConfiguration(ConfigurationMother.random())

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
        provider.setConfiguration(ConfigurationMother.random())

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
        provider.setConfiguration(ConfigurationMother.random())

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
        provider.setConfiguration(ConfigurationMother.random())

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
            status = EntityStatus.STOP
        )
        val world = WorldMother.connectedPaths(
            entities = mapOf(entity.id to entity),
            connectedPaths = mapOf(Position(3, 0) to listOf(Position(3, 4)))
        )
        provider.set(world)
        provider.setConfiguration(configuration)

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
            status = EntityStatus.STOP
        )
        val world = WorldMother.connectedPaths(
            entities = mapOf(entity.id to entity),
            connectedPaths = mapOf(Position(3, 0) to listOf(Position(3, 4)))
        )
        provider.set(world)
        provider.setConfiguration(configuration)

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
        provider.setConfiguration(ConfigurationMother.random())

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
        provider.setConfiguration(ConfigurationMother.random())

        sut.invoke(entity.id).shouldBeRight {
            assertThat(it.pax).isEqualTo(30)
        }

        assertThat(eventPublisher.publishedEvents)
            .hasSize(1)
            .containsExactly(EntityLoadedEvent(aggregateId = entity.id, amount = 20, position = Position(3, 0)))
    }
}
