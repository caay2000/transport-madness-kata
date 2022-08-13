package com.github.caay2000.ttk.api.event

interface EventSubscriber<in EVENT> {

    fun handle(event: EVENT)
}
