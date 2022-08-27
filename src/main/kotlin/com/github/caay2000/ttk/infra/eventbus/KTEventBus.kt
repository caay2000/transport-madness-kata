package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.Event
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

class KTEventBus<in COMMAND : Command, in EVENT : Event> private constructor() {

    companion object {

        private lateinit var eventBus: KTEventBus<*, *>
        fun <COMMAND : Command, EVENT : Event> init(): KTEventBus<COMMAND, EVENT> {
            eventBus = KTEventBus<COMMAND, EVENT>()
            return getInstance()
        }

        @Suppress("UNCHECKED_CAST")
        fun <COMMAND : Command, EVENT : Event> getInstance(): KTEventBus<COMMAND, EVENT> = eventBus as KTEventBus<COMMAND, EVENT>
    }

    private val commandHandlers: MutableMap<KClass<*>, List<KTCommandHandler<*>>> = mutableMapOf()
    private val eventSubscribers: MutableMap<KClass<*>, List<KTEventSubscriber<*>>> = mutableMapOf()

    internal fun subscribe(commandHandler: KTCommandHandler<@UnsafeVariance COMMAND>, type: KClass<*>) {
        commandHandlers.getOrElse(type) { listOf() }.let {
            commandHandlers[type] = it + commandHandler
        }
    }

    internal fun subscribe(subscriber: KTEventSubscriber<@UnsafeVariance EVENT>, type: KClass<*>) {
        eventSubscribers.getOrElse(type) { listOf() }.let {
            eventSubscribers[type] = it + subscriber
        }
    }

    internal fun publishCommand(command: COMMAND) {
        notifyCommandHandlers(command)
    }

    internal fun publishEvent(event: EVENT) {
        notifyEventSubscribers(event)
    }

    private fun notifyCommandHandlers(command: COMMAND) {
        commandHandlers[command::class]?.forEach { commandHandler ->
            commandHandler.execute(command)
        }
    }

    @SuppressWarnings("UNCHECKED")
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
