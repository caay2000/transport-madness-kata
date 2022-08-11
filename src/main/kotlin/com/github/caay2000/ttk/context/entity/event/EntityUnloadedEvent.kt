package com.github.caay2000.ttk.context.entity.event

import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.shared.EntityId
import java.util.UUID

data class EntityUnloadedEvent(override val aggregateId: EntityId, val amount: Int, val position: Position) : Event {
    override val eventId: UUID = UUID.randomUUID()
}
