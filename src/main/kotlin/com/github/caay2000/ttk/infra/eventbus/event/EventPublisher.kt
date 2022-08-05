package com.github.caay2000.ttk.infra.eventbus.event

interface EventPublisher<E> {
    fun publish(events: List<E>)
}
