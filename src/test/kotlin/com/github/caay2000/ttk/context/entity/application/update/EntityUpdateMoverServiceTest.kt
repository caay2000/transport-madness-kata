package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityStatus
import com.github.caay2000.ttk.context.world.application.WorldRepository
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.mother.EntityMother
import com.github.caay2000.ttk.mother.RouteMother
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class EntityUpdateMoverServiceTest {

    private val worldRepository: WorldRepository = mock()

    private val sut = EntityUpdateMoverService(worldRepository)

    @Test
    fun `entity does not move if STOPPED`() {

        `world exists`()

        sut.invoke(stoppedEntity).shouldBeRight {
            assertThat(it).isEqualTo(stoppedEntity)
        }
    }

    @Test
    fun `entity does not move if route is not assigned`() {

        `world exists`()

        sut.invoke(noRouteAssignedEntity).shouldBeRight {
            assertThat(it).isEqualTo(noRouteAssignedEntity)
        }
    }

    @Test
    fun `entity moves if it has an assigned route`() {

        `world exists`()

        sut.invoke(movingEntityWithNextSection).shouldBeRight {
            assertThat(it.currentPosition).isEqualTo(Position(1, 0))
        }
    }

    @Test
    fun `entity updates next section and moves if it has an assigned route`() {

        `world exists`()

        sut.invoke(movingEntityWithoutNextSection).shouldBeRight {
            assertThat(it.currentPosition).isEqualTo(Position(1, 0))
        }
    }

    @Test
    fun `service does not publish any event`() {

        `world exists`()

        sut.invoke(movingEntityWithoutNextSection).shouldBeRight {
            assertThat(it.pullEvents()).isEqualTo(emptyList<Event>())
        }
    }

    private fun `world exists`() {
        whenever(worldRepository.get()).thenReturn(
            WorldMother.random(connectedPaths = mapOf(Position(0, 0) to listOf(Position(3, 0)))).right()
        )
    }

    private val stoppedEntity = EntityMother.random(status = EntityStatus.STOP)
    private val noRouteAssignedEntity = EntityMother.random(status = EntityStatus.IN_ROUTE)
    private val routeWithoutNextSection = RouteMother.random(
        stops = listOf(Position(0, 0), Position(3, 0)),
        stopIndex = 1
    )
    private val routeWithNextSection = RouteMother.random(
        stops = listOf(Position(0, 0), Position(3, 0)),
        stopIndex = 1,
        nextSectionList = listOf(Cell(1, 0), Cell(2, 0))
    )
    private val movingEntityWithoutNextSection: Entity = EntityMother.random(
        currentPosition = Position(0, 0),
        route = routeWithoutNextSection,
        status = EntityStatus.IN_ROUTE
    )
    private val movingEntityWithNextSection: Entity = EntityMother.random(
        currentPosition = Position(0, 0),
        route = routeWithNextSection,
        status = EntityStatus.IN_ROUTE
    )
}
