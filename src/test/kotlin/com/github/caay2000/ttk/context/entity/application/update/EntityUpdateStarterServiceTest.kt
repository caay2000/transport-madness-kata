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
import com.github.caay2000.ttk.mother.world.location.LocationMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class EntityUpdateStarterServiceTest {

    private val provider = DefaultProvider()
    private val eventPublisher: EventPublisher<Event> = mock()
    private val sut = EntityUpdateStarterService(provider, eventPublisher)

    @Test
    fun `entity should wait x turns in STOP`() {

        `world exists`(waitingEntity)

        sut.invoke(waitingEntity).shouldBeRight {
            assertThat(it).isEqualTo(waitingEntity)
        }
    }

    @Test
    fun `entity starts movement to next route destination after configuration$turnsStoppedInStation turns in STOP`() {

        `world exists`(firstStopReadyEntity)

        sut.invoke(firstStopReadyEntity).shouldBeRight {
            assertThat(it).isEqualTo(
                firstStopReadyEntity.copy(
                    status = EntityStatus.IN_ROUTE,
                    route = routeToLastStop,
                    currentDuration = 0
                )
            )
        }
    }

    @Test
    fun `entity starts movement to initial destination after configuration$turnsStoppedInStation turns in STOP if current stop is the last one`() {

        `world exists`(lastStopReadyEntity)

        sut.invoke(lastStopReadyEntity).shouldBeRight {
            assertThat(it).isEqualTo(
                lastStopReadyEntity.copy(
                    status = EntityStatus.IN_ROUTE,
                    route = routeToInitialStop,
                    currentDuration = 0
                )
            )
        }
    }

    private fun `world exists`(entity: Entity) {
        provider.set(
            WorldMother.random(
                entities = mapOf(entity.id to entity),
                locations = mapOf(location.id to location),
                connectedPaths = mapOf(Position(3, 0) to listOf(Position(3, 4)))
            )
        )
        provider.setConfiguration(configuration)
    }

    private val configuration = ConfigurationMother.random()

    private val location = LocationMother.random(Position(3, 0))

    private val routeToLastStop = RouteMother.random(stops = listOf(Position(3, 0), Position(3, 4)), stopIndex = 0)
    private val routeToInitialStop = routeToLastStop.copy(stopIndex = 1)

    private val waitingEntity: Entity = EntityMother.random(
        currentPosition = Position(3, 0),
        currentDuration = configuration.turnsStoppedInStation,
        status = EntityStatus.STOP

    )
    private val firstStopReadyEntity: Entity = waitingEntity.copy(
        currentDuration = configuration.turnsStoppedInStation + 1,
        route = routeToInitialStop
    )
    private val lastStopReadyEntity: Entity = waitingEntity.copy(
        currentDuration = configuration.turnsStoppedInStation + 1,
        route = routeToLastStop
    )
}
