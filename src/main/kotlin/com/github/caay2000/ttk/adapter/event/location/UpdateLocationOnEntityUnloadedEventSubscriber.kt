package com.github.caay2000.ttk.adapter.event.location

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.application.location.LocationCargoUnloaderService
import com.github.caay2000.ttk.domain.entity.event.EntityUnloadedEvent
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.infra.eventbus.event.Event
import com.github.caay2000.ttk.infra.eventbus.event.EventPublisher
import com.github.caay2000.ttk.infra.eventbus.event.EventSubscriber

class UpdateLocationOnEntityUnloadedEventSubscriber(provider: Provider, eventPublisher: EventPublisher<Event>) : EventSubscriber<EntityUnloadedEvent> {

    private val locationCargoUnloaderService = LocationCargoUnloaderService(provider, eventPublisher)

    override fun handle(event: EntityUnloadedEvent): Unit =
        locationCargoUnloaderService.invoke(event.position, event.amount).void().bind()
}
