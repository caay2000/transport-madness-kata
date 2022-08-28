package com.github.caay2000.ttk.context.location.primary.event

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.event.EventSubscriber
import com.github.caay2000.ttk.context.entity.event.EntityUnloadedEvent
import com.github.caay2000.ttk.context.location.application.LocationCargoUnloaderService
import com.github.caay2000.ttk.context.location.application.LocationRepository

class UpdateLocationOnEntityUnloadedEventSubscriber(locationRepository: LocationRepository, eventPublisher: EventPublisher) : EventSubscriber<EntityUnloadedEvent> {

    private val locationCargoUnloaderService = LocationCargoUnloaderService(locationRepository, eventPublisher)

    override fun handle(event: EntityUnloadedEvent): Unit =
        locationCargoUnloaderService.invoke(event.position, event.amount).void().bind()
}
