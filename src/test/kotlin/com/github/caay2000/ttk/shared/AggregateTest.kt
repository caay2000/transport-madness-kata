package com.github.caay2000.ttk.shared

import com.github.caay2000.ttk.api.event.Event
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class AggregateTest {

    val sut = TestAggregate()

    @Test
    fun `aggregates are able to push and pull events`() {
        val event1 = TestEvent(sut.id, "hi1")
        val event2 = TestEvent(sut.id, "hi2")
        val event3 = TestEvent(sut.id, "hi3")

        sut.pushEvent(event1)
        sut.pushEvents(listOf(event2, event3))

        assertThat(sut.pullEvents()).containsExactly(event1, event2, event3)
    }

    data class TestAggregate(override val id: WorldId = randomDomainId()) : Aggregate()

    data class TestEvent(override val aggregateId: DomainId, val value: String) : Event {
        override val eventId: UUID = UUID.randomUUID()
    }
}
