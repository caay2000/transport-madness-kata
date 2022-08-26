package com.github.caay2000.ttk.context.entity.application

import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.InvalidEntityPositionException
import com.github.caay2000.ttk.context.entity.domain.PassengerTrain
import com.github.caay2000.ttk.context.world.application.WorldRepository
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.extension.thenReturnFirstArgument
import com.github.caay2000.ttk.mother.ConfigurationMother
import com.github.caay2000.ttk.mother.WorldMother
import com.github.caay2000.ttk.mother.set
import io.kotest.assertions.arrow.either.shouldBeLeftOfType
import io.kotest.assertions.arrow.either.shouldBeRight
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

internal class EntityCreatorServiceTest {

    private val worldRepository: WorldRepository = mock()
    private val entityRepository: EntityRepository = mock()
    private val eventPublisher: EventPublisher<Event> = mock()
    private val sut = EntityCreatorService(worldRepository, entityRepository, eventPublisher)

    private val configuration = ConfigurationMother.random().set()

    @Test
    fun `entity is added to world correctly`() {

        `world exists`(WorldMother.random())
        `entity will be created`()

        sut.invoke(PassengerTrain(3), Position(1, 1)).shouldBeRight {
            verify(entityRepository).save(any())
//            assertThat(provider.get().bind().entities).hasSize(1)
//            assertThat(it).isEqualTo(provider.get().bind().entities.values.first())
        }
    }

    @ParameterizedTest
    @CsvSource(value = ["-1,0", "0,-1", "1,0", "0,1"])
    fun `entity added out of bounds throw exception`(x: Int, y: Int) {
        `world exists`(WorldMother.empty(width = 1, height = 1))

        sut.invoke(PassengerTrain(), Position(x, y)).shouldBeLeftOfType<InvalidEntityPositionException>()
        verifyNoInteractions(entityRepository)
    }

    private fun `world exists`(world: World) {
        whenever(worldRepository.get()).thenReturn(world.right())
    }

    private fun `entity will be created`() {
        whenever(entityRepository.save(any())).thenReturnFirstArgument<Entity> { it.right() }
    }
}
