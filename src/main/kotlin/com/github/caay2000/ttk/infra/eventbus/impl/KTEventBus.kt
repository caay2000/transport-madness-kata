package com.github.caay2000.ttk.infra.eventbus.impl

import com.github.caay2000.ttk.infra.eventbus.event.EventBus
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

class KTEventBus<in EVENT> private constructor() : EventBus<@UnsafeVariance EVENT> {

    companion object {

        private lateinit var eventBus: KTEventBus<*>
        fun <EVENT> init(): KTEventBus<EVENT> {
            eventBus = KTEventBus<EVENT>()
            return getInstance()
        }

        @Suppress("UNCHECKED_CAST")
        fun <EVENT> getInstance(): KTEventBus<EVENT> = eventBus as KTEventBus<EVENT>
    }

    private val events: MutableList<EVENT> = mutableListOf()
    private val eventSubscribers: MutableMap<KClass<*>, List<KTEventSubscriber<*>>> = mutableMapOf()

    override fun subscribe(subscriber: KTEventSubscriber<@UnsafeVariance EVENT>, type: KClass<*>) {
        eventSubscribers.getOrElse(type) { listOf() }.let {
            eventSubscribers[type] = it + subscriber
        }
    }

    override fun publishEvent(event: EVENT) {
        events.add(event).also {
            notifyEventSubscribers(event)
        }
    }

    private fun notifyEventSubscribers(event: EVENT) {

        eventSubscribers[event!!::class]?.forEach { subscriber ->
            subscriber.execute(event)
        }

        @Suppress("NULL_CHECK")
        event!!::class.superclasses.forEach { parent ->
            eventSubscribers[parent]?.forEach { subscriber ->
                subscriber.execute(event)
            }
        }
    }

    internal fun getAllEvents(): List<@UnsafeVariance EVENT> = events.toList()
}
