package com.github.caay2000.ttk.infra.eventbus

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EventBusTest {

    @Test
    internal fun `subscribers receive the published event`() {

        KTEventBus.init<String>()
        val sut = StringSubscriber()
        KTEventPublisher<String>().publish(listOf("hi"))

        assertThat(sut.events).isEqualTo(listOf("hi"))
    }

    @Test
    internal fun `multiple subscribers receive the published event`() {

        KTEventBus.init<String>()
        val subscriber1 = StringSubscriber()
        val subscriber2 = StringSubscriber()
        KTEventPublisher<String>().publish(listOf("hi"))

        assertThat(subscriber1.events).isEqualTo(listOf("hi"))
        assertThat(subscriber2.events).isEqualTo(listOf("hi"))
    }

    @Test
    internal fun `subscriber of different type does not receive the event`() {

        KTEventBus.init<Number>()
        val sut = IntSubscriber()
        KTEventPublisher<Number>().publish(listOf(Double.MAX_VALUE))

        println(sut.events)
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
