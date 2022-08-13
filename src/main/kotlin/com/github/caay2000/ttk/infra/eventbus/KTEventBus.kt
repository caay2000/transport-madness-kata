package com.github.caay2000.ttk.infra.eventbus

import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

class KTEventBus<in EVENT> private constructor() {

    companion object {

        private lateinit var eventBus: KTEventBus<*>
        fun <EVENT> init(): KTEventBus<EVENT> {
            eventBus = KTEventBus<EVENT>()
            return getInstance()
        }

        @Suppress("UNCHECKED_CAST")
        fun <EVENT> getInstance(): KTEventBus<EVENT> = eventBus as KTEventBus<EVENT>
    }

    private val eventSubscribers: MutableMap<KClass<*>, List<KTEventSubscriber<*>>> = mutableMapOf()

    internal fun subscribe(subscriber: KTEventSubscriber<@UnsafeVariance EVENT>, type: KClass<*>) {
        eventSubscribers.getOrElse(type) { listOf() }.let {
            eventSubscribers[type] = it + subscriber
        }
    }

    internal fun publishEvent(event: EVENT) {
        notifyEventSubscribers(event)
    }

    @SuppressWarnings("UNCHECKED")
    private fun notifyEventSubscribers(event: EVENT) {

        eventSubscribers[event!!::class]?.forEach { subscriber ->
            subscriber.execute(event)
        }

        event!!::class.superclasses.forEach { parent ->
            eventSubscribers[parent]?.forEach { subscriber ->
                subscriber.execute(event)
            }
        }
    }
}
