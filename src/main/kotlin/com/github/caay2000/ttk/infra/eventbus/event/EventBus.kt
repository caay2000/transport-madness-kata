package com.github.caay2000.ttk.infra.eventbus.event

import com.github.caay2000.ttk.infra.eventbus.impl.KTEventSubscriber
import kotlin.reflect.KClass

interface EventBus<T> {

    fun subscribe(subscriber: KTEventSubscriber<T>, type: KClass<*>)
    fun publishEvent(event: T)
}
