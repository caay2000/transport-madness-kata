package com.github.caay2000.ttk.context.entity.application.update

import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityStatus
import com.github.caay2000.ttk.context.location.primary.query.LocationFinderQuery
import com.github.caay2000.ttk.context.location.primary.query.LocationFinderQueryResponse
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.mother.ConfigurationMother
import com.github.caay2000.ttk.mother.EntityMother
import com.github.caay2000.ttk.mother.RouteMother
import com.github.caay2000.ttk.mother.set
import com.github.caay2000.ttk.mother.world.location.LocationMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class EntityUpdateStarterServiceTest {

    private val queryExecutor: QueryExecutor = mock()
    private val eventPublisher: EventPublisher = mock()
    private val sut = EntityUpdateStarterService(queryExecutor, eventPublisher)

    @Test
    fun `entity should wait x turns in STOP`() {

        `world exists`()

        sut.invoke(waitingEntity).shouldBeRight {
            assertThat(it).isEqualTo(waitingEntity)
        }
    }

    @Test
    fun `entity starts movement to next route destination after configuration$turnsStoppedInStation turns in STOP`() {

        `world exists`()

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

        `world exists`()

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

    @Test
    fun `service does not publish any event`() {

        `world exists`()

        sut.invoke(lastStopReadyEntity).shouldBeRight {
            verify(eventPublisher).publish(emptyList())
        }
    }

    private fun `world exists`() {
        whenever(queryExecutor.execute<LocationFinderQuery, LocationFinderQueryResponse>(any())).thenReturn(LocationFinderQueryResponse(location))
    }

    private val configuration = ConfigurationMother.random().set()

    private val location = LocationMother.random(position = Position(3, 0))

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
