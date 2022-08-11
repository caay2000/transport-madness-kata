package com.github.caay2000.ttk.infra.eventbus

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EventBusTest {

    @Test
    internal fun `subscribers receive the published event`() {

        KTEventBus.init<Any, String>()
        val sut = StringSubscriber()
        KTEventPublisher<String>().publish(listOf("hi"))

        assertThat(sut.events).isEqualTo(listOf("hi"))
    }

    @Test
    internal fun `multiple subscribers receive the published event`() {

        val subscriber1 = StringSubscriber()
        val subscriber2 = StringSubscriber()
        KTEventPublisher<String>().publish(listOf("hi"))

        assertThat(subscriber1.events).isEqualTo(listOf("hi"))
        assertThat(subscriber2.events).isEqualTo(listOf("hi"))
    }

    inner class StringSubscriber : KTEventSubscriber<String>(String::class) {
        val events = mutableListOf<String>()
        override fun handle(event: String) {
            events.add(event)
        }
    }
}
