package com.github.caay2000.ttk.context.location.primary

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.event.EventSubscriber
import com.github.caay2000.ttk.context.entity.event.EntityLoadedEvent
import com.github.caay2000.ttk.context.location.application.LocationCargoLoaderService
import com.github.caay2000.ttk.context.location.application.LocationRepository

class UpdateLocationOnEntityLoadedEventSubscriber(locationRepository: LocationRepository, eventPublisher: EventPublisher<Event>) : EventSubscriber<EntityLoadedEvent> {

    private val locationCargoLoaderService = LocationCargoLoaderService(locationRepository, eventPublisher)

    override fun handle(event: EntityLoadedEvent) =
        locationCargoLoaderService.invoke(event.position, event.amount).void().bind()
}
