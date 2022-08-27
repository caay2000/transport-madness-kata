package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher

class KTEventPublisher<in EVENT : Event> : EventPublisher<EVENT> {

    override fun publish(events: List<EVENT>) {
        events.forEach { event ->
            publish(event)
        }
    }

    private fun publish(event: EVENT) = KTEventBus.getInstance<Command, EVENT>().publishEvent(event)
}
