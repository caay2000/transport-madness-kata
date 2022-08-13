package com.github.caay2000.ttk.context.entity.application.update

import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityStatus
import com.github.caay2000.ttk.context.entity.event.EntityLoadedEvent
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mother.EntityMother
import com.github.caay2000.ttk.mother.WorldMother
import com.github.caay2000.ttk.mother.world.location.LocationMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

internal class EntityUpdateLoaderServiceTest {

    private val provider = DefaultProvider()
    private val eventPublisher: EventPublisher<Event> = mock()
    private val sut = EntityUpdateLoaderService(provider, eventPublisher)

    @Test
    fun `should load passengers (event) when in station for more than 1 turn`() {

        val entity: Entity = EntityMother.random(
            currentPosition = Position(3, 0),
            currentDuration = 1,
            pax = 10,
            status = EntityStatus.STOP
        )
        val location: Location = LocationMother.random(position = Position(3, 0), rawPAX = 20.0)
        val world = WorldMother.empty(
            entities = mapOf(entity.id to entity),
            locations = mapOf(location.id to location)
        )
        provider.set(world)

        sut.invoke(entity).shouldBeRight {
            Assertions.assertThat(it.pax).isEqualTo(30)
        }

        verify(eventPublisher).publish(
            listOf(EntityLoadedEvent(aggregateId = entity.id, amount = 20, position = Position(3, 0)))
        )
    }
}
