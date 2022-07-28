package com.github.caay2000.ttk.application.entity

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.infra.provider.DefaultWorldProvider
import com.github.caay2000.ttk.mother.EntityMother
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EntityDestinationAssignerServiceTest {

    private val provider = DefaultWorldProvider()
    private val sut = EntityDestinationAssignerService(provider)

    @Test
    fun `destination is added to entity`() {

        val entity = EntityMother.random()
        val world = WorldMother.oneVehicle(entity = entity)
        provider.set(world)

        sut.invoke(entity.id, Position(1, 1)).shouldBeRight {
            assertThat(it.getEntity(entity.id)).isEqualTo(entity.copy(destination = Position(1, 1)))
            assertThat(it).isEqualTo(provider.get().bind())
        }
    }
}
