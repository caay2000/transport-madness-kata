package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.event.Query

class KTEventPublisher : EventPublisher {

    override fun <EVENT : Event> publish(events: List<EVENT>) {
        events.forEach { event ->
            publish(event)
        }
    }

    private fun <EVENT : Event> publish(event: EVENT) = KTEventBus.getInstance<Command, Query, EVENT>().publishEvent(event)
}
