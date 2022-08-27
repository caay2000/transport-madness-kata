package com.github.caay2000.ttk.api.event

interface EventPublisher<in EVENT : Event> {

    fun publish(events: List<EVENT>)
}
