package com.github.caay2000.ttk.api.event

interface EventSubscriber<in EVENT : Event> {

    fun handle(event: EVENT)
}
