package com.github.caay2000.ttk.infra.eventbus.impl

class KTEventPublisher<in EVENT> {

    fun publish(event: EVENT) = KTEventBus.getInstance<EVENT>().publishEvent(event)
}
