package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.infra.eventbus.event.EventPublisherImpl
import com.github.caay2000.ttk.infra.eventbus.impl.KTEventBus
import com.github.caay2000.ttk.infra.eventbus.impl.KTEventSubscriber
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EventBusTest {

    @Test
    internal fun `event is published to event bus`() {

        val eventBus = KTEventBus.init<String>()
        EventPublisherImpl<String>().publish(listOf("hi"))

        assertThat(eventBus.getAllEvents()).hasSize(1)
            .isEqualTo(listOf("hi"))
    }

    @Test
    internal fun `subscribers receive the published event`() {

        KTEventBus.init<String>()
        val sut = StringSubscriber()
        EventPublisherImpl<String>().publish(listOf("hi"))

        assertThat(sut.events).isEqualTo(listOf("hi"))
    }

    @Test
    internal fun `multiple subscribers receive the published event`() {

        val eventBus = KTEventBus.init<String>()
        val subscriber1 = StringSubscriber()
        val subscriber2 = StringSubscriber()
        EventPublisherImpl<String>().publish(listOf("hi"))

        assertThat(subscriber1.events).isEqualTo(listOf("hi"))
        assertThat(subscriber2.events).isEqualTo(listOf("hi"))
        assertThat(eventBus.getAllEvents()).isEqualTo(listOf("hi"))
    }

    @Test
    internal fun `subscriber of different type does not receive the event`() {

        val eventBus = KTEventBus.init<String>()
        val sut = IntSubscriber()
        EventPublisherImpl<Number>().publish(listOf(Double.MAX_VALUE))

        assertThat(eventBus.getAllEvents()).hasSize(1)
        assertThat(sut.events).hasSize(0)
    }

    inner class StringSubscriber : KTEventSubscriber<String>(String::class) {
        val events = mutableListOf<String>()
        override fun handle(event: String) {
            events.add(event)
        }
    }

    inner class IntSubscriber : KTEventSubscriber<Int>(Int::class) {
        val events = mutableListOf<Int>()
        override fun handle(event: Int) {
            events.add(event)
        }
    }
}
