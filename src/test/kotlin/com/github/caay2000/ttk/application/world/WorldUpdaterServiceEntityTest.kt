package com.github.caay2000.ttk.application.world

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.entity.EntityStatus
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mother.ConfigurationMother
import com.github.caay2000.ttk.mother.EntityMother
import com.github.caay2000.ttk.mother.RouteMother
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class WorldUpdaterServiceEntityTest {

    private val provider = DefaultProvider()
    private val sut = WorldUpdaterService(provider)

    @Test
    fun `entity does not move if not needed`() {

        val entity: Entity = EntityMother.random()
        val world = WorldMother.oneVehicle(entity = entity)
        provider.set(world)

        sut.invoke().shouldBeRight {
            assertThat(it.getEntity(entity.id)).isEqualTo(entity.copy(currentDuration = 1))
        }
    }

    @Test
    fun `entity moves if it has an assigned route`() {

        val entity: Entity = EntityMother.random(currentPosition = Position(0, 0), route = RouteMother.random(Position(3, 0)), currentDuration = 1)
        val world = WorldMother.oneVehicle(entity = entity)
        provider.set(world)

        sut.invoke().shouldBeRight {
            assertThat(it.getEntity(entity.id)).isEqualTo(entity.copy(currentPosition = Position(1, 0), status = EntityStatus.IN_ROUTE))
            assertThat(it).isEqualTo(provider.get().bind())
        }
    }

    @Test
    fun `entity should update status to STOP when reaches a stop`() {

        val entity: Entity = EntityMother.random(
            currentPosition = Position(2, 0),
            route = RouteMother.random(Position(3, 0)),
            status = EntityStatus.IN_ROUTE
        )
        val world = WorldMother.oneVehicle(entity = entity)
        provider.set(world)

        sut.invoke().shouldBeRight {
            assertThat(it.getEntity(entity.id)).isEqualTo(entity.copy(currentPosition = Position(3, 0), status = EntityStatus.STOP))
            assertThat(it).isEqualTo(provider.get().bind())
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

        sut.invoke().shouldBeRight {
            assertThat(it.getEntity(entity.id)).isEqualTo(entity.copy(currentPosition = Position(3, 0), status = EntityStatus.STOP, currentDuration = 1))
            assertThat(it).isEqualTo(provider.get().bind())
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
        val world = WorldMother.oneVehicle(entity = entity)
        provider.set(world)

        sut.invoke().shouldBeRight {
            assertThat(it.getEntity(entity.id)).isEqualTo(
                entity.copy(
                    currentPosition = Position(3, 1),
                    route = route.copy(stopIndex = 1),
                    status = EntityStatus.IN_ROUTE
                )
            )
            assertThat(it).isEqualTo(provider.get().bind())
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
        val world = WorldMother.oneVehicle(entity = entity)
        provider.set(world)

        sut.invoke().shouldBeRight {
            assertThat(it.getEntity(entity.id)).isEqualTo(
                entity.copy(
                    currentPosition = Position(3, 3),
                    route = route.copy(stopIndex = 0),
                    status = EntityStatus.IN_ROUTE
                )
            )
            assertThat(it).isEqualTo(provider.get().bind())
        }
    }
}
