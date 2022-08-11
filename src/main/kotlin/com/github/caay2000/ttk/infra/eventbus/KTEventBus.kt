package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.DomainQueryExecutor
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

class KTEventBus<in QUERY, in EVENT> private constructor() {

    companion object {

        private lateinit var eventBus: KTEventBus<*, *>
        fun <QUERY, EVENT> init(): KTEventBus<QUERY, EVENT> {
            eventBus = KTEventBus<QUERY, EVENT>()
            DomainQueryExecutor.init(KTQueryExecutor())
            return getInstance()
        }

        @Suppress("UNCHECKED_CAST")
        fun <QUERY, EVENT> getInstance(): KTEventBus<QUERY, EVENT> = eventBus as KTEventBus<QUERY, EVENT>
    }

    private val queries: MutableList<QUERY> = mutableListOf()
    private val queryHandlers: MutableMap<KClass<*>, KTQueryHandler<*, *>> = mutableMapOf()

    private val events: MutableList<EVENT> = mutableListOf()
    private val eventSubscribers: MutableMap<KClass<*>, List<KTEventSubscriber<*>>> = mutableMapOf()

    internal fun subscribe(queryHandler: KTQueryHandler<*, *>, type: KClass<*>) {
        queryHandlers.getOrElse(type) { null }.let {
            queryHandlers[type] = queryHandler
        }
    }

    internal fun subscribe(subscriber: KTEventSubscriber<@UnsafeVariance EVENT>, type: KClass<*>) {
        eventSubscribers.getOrElse(type) { listOf() }.let {
            eventSubscribers[type] = it + subscriber
        }
    }

    internal fun <RESPONSE> publishQuery(query: QUERY): RESPONSE =
        queries.add(query)
            .let {
                @Suppress("UNCHECKED_CAST")
                queryHandlers[query!!::class]!!.execute(query) as RESPONSE
            }

    internal fun publishEvent(event: EVENT) {
        events.add(event).also {
            notifyEventSubscribers(event)
        }
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

    internal fun getAllEvents(): List<@UnsafeVariance EVENT> = events.toList()
}
