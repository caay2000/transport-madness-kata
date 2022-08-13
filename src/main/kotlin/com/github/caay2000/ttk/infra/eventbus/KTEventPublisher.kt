package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.EventPublisher

class KTEventPublisher<in EVENT> : EventPublisher<EVENT> {

    override fun publish(events: List<EVENT>) {
        events.forEach { event ->
//            println(event)
            publish(event)
        }
    }

    private fun publish(event: EVENT) = KTEventBus.getInstance<EVENT>().publishEvent(event)
}
