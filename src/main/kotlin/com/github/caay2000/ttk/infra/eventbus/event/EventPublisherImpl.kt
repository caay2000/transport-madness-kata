package com.github.caay2000.ttk.infra.eventbus.event

import com.github.caay2000.ttk.infra.eventbus.impl.KTEventPublisher

class EventPublisherImpl<T> : EventPublisher<T> {

    private val eventPublisher = KTEventPublisher<T>()

    override fun publish(events: List<T>) =
        events.forEach { event ->
            eventPublisher.publish(event)
        }
}
