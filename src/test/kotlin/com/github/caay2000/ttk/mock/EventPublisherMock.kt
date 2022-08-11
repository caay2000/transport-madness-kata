package com.github.caay2000.ttk.mock

import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher

class EventPublisherMock : EventPublisher<Event> {

    private val events: MutableList<Event> = mutableListOf()

    val publishedEvents: List<Event>
        get() = events

    override fun publish(events: List<Event>) {
        this.events.addAll(events)
    }
}
