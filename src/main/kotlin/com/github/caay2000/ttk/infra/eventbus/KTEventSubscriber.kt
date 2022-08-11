package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventSubscriber
import kotlin.reflect.KClass

abstract class KTEventSubscriber<in EVENT>(type: KClass<*>) {

    init {
        subscribeTo(type)
    }

    private fun subscribeTo(type: KClass<*>) {
        KTEventBus.getInstance<Any, EVENT>().subscribe(this, type)
    }

    internal fun execute(event: Any) {
        @Suppress("UNCHECKED_CAST")
        this.handle(event as EVENT)
    }

    abstract fun handle(event: EVENT)
}

fun <EVENT : Event> instantiateEventSubscriber(clazz: KClass<EVENT>, eventSubscriber: EventSubscriber<EVENT>): KTEventSubscriber<EVENT> =
    object : KTEventSubscriber<EVENT>(clazz) {
        override fun handle(event: EVENT) = eventSubscriber.handle(event)
    }
