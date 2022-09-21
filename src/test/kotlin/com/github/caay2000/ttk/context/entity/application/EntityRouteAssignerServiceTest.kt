package com.github.caay2000.ttk.context.entity.application

import arrow.core.right
import com.github.caay2000.ttk.context.entity.application.EntityRepository.FindEntityCriteria.ById
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.InvalidRouteEntityException
import com.github.caay2000.ttk.context.entity.domain.Route
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.extension.thenReturnFirstArgument
import com.github.caay2000.ttk.mother.EntityMother
import io.kotest.assertions.arrow.either.shouldBeLeftOfType
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class EntityRouteAssignerServiceTest {

    private val entityRepository: EntityRepository = mock()
    private val sut = EntityRouteAssignerService(entityRepository, mock())

    @Test
    fun `route is added to entity`() {

        `entity exists`()
        `entity will be updated`()

        val stops = listOf(Position(1, 1), Position(3, 2))
        sut.invoke(entity.id, stops).shouldBeRight {
            verify(entityRepository).save(it)
            assertThat(it).isEqualTo(entity.copy(route = Route(stops)))
        }
    }

    @Test
    fun `should fail if route is invalid`() {

        `entity exists`()

        sut.invoke(entity.id, emptyList()).shouldBeLeftOfType<InvalidRouteEntityException>()
        verify(entityRepository, never()).save(any())
    }

    private fun `entity exists`() {
        whenever(entityRepository.find(ById(entity.id))).thenReturn(entity.right())
    }

    private fun `entity will be updated`() {
        whenever(entityRepository.save(any())).thenReturnFirstArgument<Entity> { it.right() }
    }

    private val entity = EntityMother.random()
}
