package com.github.caay2000.ttk.application.entity

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.infra.provider.DefaultWorldProvider
import com.github.caay2000.ttk.mother.WorldMother
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class EntityDestinationAssignerServiceTest {

    private val provider = DefaultWorldProvider()
    private val sut = EntityDestinationAssignerService(provider)

    @Test
    @Disabled
    fun `destination is added to entity`() {

        val entityId: EntityId = randomDomainId()
        val world = WorldMother.oneVehicle(entityId = entityId)
        provider.set(world)

        sut.invoke(entityId, Position(1, 1)).shouldBeRight {
            val entity = it.getEntity(entityId)
            assertThat(entity.destination).isEqualTo(Position(1, 1))
            assertThat(entity).isEqualTo(provider.get().bind().getEntity(entityId))
        }
    }
}
