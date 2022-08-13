package com.github.caay2000.ttk.context.location.event

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.event.EventSubscriber
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.event.EntityUnloadedEvent
import com.github.caay2000.ttk.context.location.application.LocationCargoUnloaderService

class UpdateLocationOnEntityUnloadedEventSubscriber(provider: Provider, eventPublisher: EventPublisher<Event>) : EventSubscriber<EntityUnloadedEvent> {

    private val locationCargoUnloaderService = LocationCargoUnloaderService(provider, eventPublisher)

    override fun handle(event: EntityUnloadedEvent): Unit =
        locationCargoUnloaderService.invoke(event.position, event.amount).void().bind()
}
