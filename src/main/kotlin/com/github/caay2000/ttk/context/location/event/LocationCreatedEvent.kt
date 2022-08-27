package com.github.caay2000.ttk.context.location.event

import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.shared.LocationId
import java.util.UUID

data class LocationCreatedEvent(
    override val aggregateId: LocationId,
    val name: String,
    val position: Position
) : Event {
    override val eventId: UUID = UUID.randomUUID()
}
