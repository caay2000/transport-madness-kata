package com.github.caay2000.ttk.adapter.event.location

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.application.location.LocationCargoLoaderService
import com.github.caay2000.ttk.domain.entity.event.EntityLoadedEvent
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.infra.eventbus.event.Event
import com.github.caay2000.ttk.infra.eventbus.event.EventPublisher
import com.github.caay2000.ttk.infra.eventbus.event.EventSubscriber

class UpdateLocationOnEntityLoadedEventSubscriber(provider: Provider, eventPublisher: EventPublisher<Event>) : EventSubscriber<EntityLoadedEvent> {

    private val locationCargoLoaderService = LocationCargoLoaderService(provider, eventPublisher)

    override fun handle(event: EntityLoadedEvent) =
        locationCargoLoaderService.invoke(event.position, event.amount).void().bind()
}
