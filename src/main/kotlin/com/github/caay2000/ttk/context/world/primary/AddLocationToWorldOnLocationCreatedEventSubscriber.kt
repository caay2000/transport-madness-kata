package com.github.caay2000.ttk.context.world.primary

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.event.EventSubscriber
import com.github.caay2000.ttk.context.location.event.LocationCreatedEvent
import com.github.caay2000.ttk.context.world.application.WorldLocationAdderService
import com.github.caay2000.ttk.context.world.application.WorldRepository

class AddLocationToWorldOnLocationCreatedEventSubscriber(worldRepository: WorldRepository, eventPublisher: EventPublisher) : EventSubscriber<LocationCreatedEvent> {

    private val worldLocationAdderService = WorldLocationAdderService(worldRepository, eventPublisher)

    override fun handle(event: LocationCreatedEvent) =
        worldLocationAdderService.invoke(event.aggregateId, event.position).void().bind()
}
