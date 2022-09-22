package com.github.caay2000.ttk.context.entity.application.update

import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityStatus
import com.github.caay2000.ttk.context.entity.event.EntityUnloadedEvent
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.mother.EntityMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EntityUpdateUnloaderServiceTest {

    private val sut = EntityUpdateUnloaderService()

    @Test
    fun `should do nothing if entity is IN_ROUTE`() {

        sut.invoke(movingEntity).shouldBeRight {
            assertThat(it).isEqualTo(movingEntity)
        }
    }

    @Test
    fun `should not load passengers when in station for less than 1 turn`() {

        sut.invoke(alreadyUnloadedEntity).shouldBeRight {
            assertThat(it).isEqualTo(alreadyUnloadedEntity)
        }
    }

    @Test
    fun `should unload passengers when reaches a station`() {

        sut.invoke(loadedEntity).shouldBeRight {
            assertThat(it.pax).isEqualTo(0)
        }
    }

    @Test
    fun `should publish an event when unloads passengers`() {

        sut.invoke(loadedEntity).shouldBeRight {
            assertThat(it.pullEvents()).isEqualTo(
                listOf(EntityUnloadedEvent(loadedEntity.id, amount = 10, position = it.currentPosition))
            )
        }
    }

    @Test
    fun `shouldn't publish an event when entity is empty`() {

        sut.invoke(unloadedEntity).shouldBeRight {
            assertThat(it.pullEvents()).isEqualTo(emptyList<Event>())
        }
    }

    private val loadedEntity = EntityMother.random(
        currentPosition = Position(3, 0),
        pax = 10,
        status = EntityStatus.STOP
    )
    private val unloadedEntity = EntityMother.random(
        currentPosition = Position(3, 0),
        pax = 0,
        status = EntityStatus.STOP
    )
    private val alreadyUnloadedEntity: Entity = EntityMother.random(
        currentPosition = Position(3, 0),
        currentDuration = 1,
        status = EntityStatus.STOP
    )
    private val movingEntity: Entity = EntityMother.random(
        status = EntityStatus.IN_ROUTE
    )
}
