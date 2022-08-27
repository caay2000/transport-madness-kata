package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryResponse
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

class KTEventBus<in COMMAND : Command, in QUERY : Query, in EVENT : Event> private constructor() {

    companion object {

        private lateinit var eventBus: KTEventBus<Command, Query, Event>
        fun <COMMAND : Command, QUERY : Query, EVENT : Event> init(): KTEventBus<COMMAND, QUERY, EVENT> {
            eventBus = KTEventBus()
            return getInstance()
        }

        @Suppress("UNCHECKED_CAST")
        fun <COMMAND : Command, QUERY : Query, EVENT : Event> getInstance(): KTEventBus<COMMAND, QUERY, EVENT> = eventBus
    }

    private val commandHandlers: MutableMap<KClass<COMMAND>, KTCommandHandler<COMMAND>> = mutableMapOf()
    private val queryHandlers: MutableMap<KClass<QUERY>, KTQueryHandler<QUERY, *>> = mutableMapOf()
    private val eventSubscribers: MutableMap<KClass<EVENT>, List<KTEventSubscriber<EVENT>>> = mutableMapOf()

    internal fun subscribe(commandHandler: KTCommandHandler<@UnsafeVariance COMMAND>, type: KClass<@UnsafeVariance COMMAND>) {
        commandHandlers[type] = commandHandler
    }

    internal fun <RESPONSE : QueryResponse> subscribe(queryHandler: KTQueryHandler<@UnsafeVariance QUERY, RESPONSE>, type: KClass<@UnsafeVariance QUERY>) {
        queryHandlers[type] = queryHandler
    }

    internal fun subscribe(subscriber: KTEventSubscriber<@UnsafeVariance EVENT>, type: KClass<@UnsafeVariance EVENT>) {
        eventSubscribers.getOrElse(type) { listOf() }.let {
            eventSubscribers[type] = it + subscriber
        }
    }

    internal fun publishCommand(command: COMMAND) {
        commandHandlers[command::class]?.execute(command)
            ?: throw RuntimeException("commandHandler for $command not found")
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <RESPONSE : QueryResponse> executeQuery(query: QUERY): @UnsafeVariance RESPONSE =
        queryHandlers[query::class]?.let {
            (it as KTQueryHandler<QUERY, RESPONSE>).execute(query) as RESPONSE
        } ?: throw RuntimeException("queryHandler for $query not found")

    internal fun publishEvent(event: EVENT) {
        notifyEventSubscribers(event)
    }

    private fun notifyEventSubscribers(event: EVENT) {

        eventSubscribers[event::class]?.forEach { subscriber ->
            subscriber.execute(event)
        }

        event::class.superclasses.forEach { parent ->
            eventSubscribers[parent]?.forEach { subscriber ->
                subscriber.execute(event)
            }
        }
    }
}
