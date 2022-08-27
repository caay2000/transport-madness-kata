package com.github.caay2000.ttk.context.world.application

import arrow.core.right
import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.CommandBus
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.domain.UnknownCompanyException
import com.github.caay2000.ttk.context.company.primary.command.UpdateAllCompaniesCommand
import com.github.caay2000.ttk.context.location.domain.UnknownLocationException
import com.github.caay2000.ttk.context.location.primary.command.UpdateAllLocationsCommand
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.extension.thenReturnFirstArgument
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class WorldUpdaterServiceTest {

    private val worldRepository: WorldRepository = mock()
    private val commandBus: CommandBus<Command> = mock()
    private val eventPublisher: EventPublisher<Event> = mock()
    private val sut = WorldUpdaterService(worldRepository, commandBus, eventPublisher)

    @Test
    fun `turn is updated correctly`() {

        `world exists`()
        `world will be updated`()

        sut.invoke().shouldBeRight {
            verify(worldRepository).save(world.copy(currentTurn = 1))
            verify(commandBus).publish(any<UpdateAllLocationsCommand>())
            verify(commandBus).publish(any<UpdateAllCompaniesCommand>())
        }
    }

    @Test
    fun `should fail if location command fails`() {

        `world exists`()
        `updateAllLocationsCommand will fail`()

        assertThrows<UnknownLocationException> {
            sut.invoke()
        }
        verify(worldRepository, never()).save(any())
        verify(commandBus).publish(any<UpdateAllLocationsCommand>())
        verify(commandBus, never()).publish(any<UpdateAllCompaniesCommand>())
    }

    @Test
    fun `should fail if companies command fails`() {

        `world exists`()
        `updateAllCompaniesCommand will fail`()

        assertThrows<UnknownCompanyException> {
            sut.invoke()
        }
        verify(worldRepository, never()).save(any())
        verify(commandBus).publish(any<UpdateAllLocationsCommand>())
        verify(commandBus).publish(any<UpdateAllCompaniesCommand>())
    }

    private fun `world exists`() {
        whenever(worldRepository.get()).thenReturn(world.right())
    }

    private fun `world will be updated`() {
        whenever(worldRepository.save(any())).thenReturnFirstArgument<World> { it.right() }
    }

    private fun `updateAllLocationsCommand will fail`() {
        whenever(commandBus.publish(any<UpdateAllLocationsCommand>())).thenThrow(UnknownLocationException(RuntimeException()))
    }

    private fun `updateAllCompaniesCommand will fail`() {
        whenever(commandBus.publish(any<UpdateAllCompaniesCommand>())).thenThrow(UnknownCompanyException(RuntimeException()))
    }

    private val world = WorldMother.random()
}
