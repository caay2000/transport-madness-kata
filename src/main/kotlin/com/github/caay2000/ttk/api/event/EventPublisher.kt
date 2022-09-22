package com.github.caay2000.ttk.api.event

interface EventPublisher {

    fun <EVENT : Event> publish(events: List<EVENT>)
}
