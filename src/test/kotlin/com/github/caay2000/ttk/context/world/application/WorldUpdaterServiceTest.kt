package com.github.caay2000.ttk.context.world.application

import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.application.EntityRepository
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.location.application.LocationRepository
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.extension.thenReturnFirstArgument
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class WorldUpdaterServiceTest {

    private val worldRepository: WorldRepository = mock()
    private val locationRepository: LocationRepository = mock()
    private val entityRepository: EntityRepository = mock()
    private val eventPublisher: EventPublisher<Event> = mock()
    private val sut = WorldUpdaterService(worldRepository, locationRepository, entityRepository, eventPublisher)

    @Test
    fun `turn is updated correctly`() {

        `world exists`()
        `world will be updated`()
        `no locations exists`()
        `no entities exists`()

        sut.invoke().shouldBeRight {
            verify(worldRepository).save(world.copy(currentTurn = 1))
        }
    }

    private fun `world exists`() {
        whenever(worldRepository.get()).thenReturn(world.right())
    }

    private fun `world will be updated`() {
        whenever(worldRepository.save(any())).thenReturnFirstArgument<World> { it.right() }
    }

    private fun `no locations exists`() {
        whenever(locationRepository.findAll()).thenReturn(emptyList<Location>().right())
    }

    private fun `no entities exists`() {
        whenever(entityRepository.findAll()).thenReturn(emptyList<Entity>().right())
    }

    private val world = WorldMother.random()
}
