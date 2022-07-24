package com.github.caay2000.ttk.application.entity

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.domain.entity.Route
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mother.EntityMother
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EntityRouteAssignerServiceTest {

    private val provider = DefaultProvider()
    private val sut = EntityRouteAssignerService(provider)

    @Test
    fun `destination is added to entity`() {

        val entity = EntityMother.random()
        val world = WorldMother.oneVehicle(entity = entity)
        provider.set(world)

        val stops = listOf(Position(1, 1), Position(3, 2))
        sut.invoke(entity.id, stops).shouldBeRight {
            assertThat(it.getEntity(entity.id)).isEqualTo(entity.copy(route = Route(stops)))
            assertThat(it).isEqualTo(provider.get().bind())
        }
    }
}
