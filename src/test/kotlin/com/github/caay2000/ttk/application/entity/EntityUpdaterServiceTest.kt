package com.github.caay2000.ttk.application.entity

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.infra.provider.DefaultWorldProvider
import com.github.caay2000.ttk.mother.EntityMother
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EntityUpdaterServiceTest {

    private val provider = DefaultWorldProvider()
    private val sut = EntityUpdaterService(provider)

    @Test
    fun `entity does not move if not needed`() {

        val entity: Entity = EntityMother.random()
        val world = WorldMother.oneVehicle(entity = entity)
        provider.set(world)

        sut.invoke(entity.id).shouldBeRight {
            assertThat(it.getEntity(entity.id)).isEqualTo(entity)
        }
    }

    @Test
    fun `entity moves if it has destination`() {

        val entity: Entity = EntityMother.random(currentPosition = Position(0, 0), destination = Position(3, 0))
        val world = WorldMother.oneVehicle(entity = entity)
        provider.set(world)

        sut.invoke(entity.id).shouldBeRight {
            assertThat(it.getEntity(entity.id)).isEqualTo(entity.copy(currentPosition = Position(1, 0)))
            assertThat(it).isEqualTo(provider.get().bind())
        }
    }
}
