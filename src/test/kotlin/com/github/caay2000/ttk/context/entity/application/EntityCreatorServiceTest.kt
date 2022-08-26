package com.github.caay2000.ttk.context.entity.application

import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.application.CompanyRepository
import com.github.caay2000.ttk.context.company.application.CompanyRepository.FindCompanyCriteria.ByIdCriteria
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.InvalidEntityPositionException
import com.github.caay2000.ttk.context.entity.domain.PassengerTrain
import com.github.caay2000.ttk.context.world.application.WorldRepository
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.extension.thenReturnFirstArgument
import com.github.caay2000.ttk.mother.CompanyMother
import com.github.caay2000.ttk.mother.EntityCreatedEventMother
import com.github.caay2000.ttk.mother.WorldMother
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
    private val companyRepository: CompanyRepository = mock()
    private val entityRepository: EntityRepository = mock()
    private val eventPublisher: EventPublisher<Event> = mock()
    private val sut = EntityCreatorService(worldRepository, companyRepository, entityRepository, eventPublisher)

    @Test
    fun `entity is created correctly`() {

        `world exists`(WorldMother.random())
        `company exists`()
        `entity will be created`()

        sut.invoke(
            companyId = company.id,
            entityType = PassengerTrain(3),
            position = Position(1, 1)
        ).shouldBeRight {
            verify(entityRepository).save(any())
            verify(eventPublisher).publish(listOf(EntityCreatedEventMother.from(it)))
        }
    }

    @ParameterizedTest
    @CsvSource(value = ["-1,0", "0,-1", "1,0", "0,1"])
    fun `entity added out of bounds throw exception`(x: Int, y: Int) {

        `world exists`(WorldMother.empty(width = 1, height = 1))
        `company exists`()

        sut.invoke(
            companyId = company.id,
            entityType = PassengerTrain(),
            position = Position(x, y)
        ).shouldBeLeftOfType<InvalidEntityPositionException>()
        verifyNoInteractions(entityRepository)
    }

    private fun `world exists`(world: World) {
        whenever(worldRepository.get()).thenReturn(world.right())
    }

    private fun `company exists`() {
        whenever(companyRepository.exists(ByIdCriteria(company.id))).thenReturn(true)
    }

    private fun `entity will be created`() {
        whenever(entityRepository.save(any())).thenReturnFirstArgument<Entity> { it.right() }
    }

    private val company = CompanyMother.random()
}
