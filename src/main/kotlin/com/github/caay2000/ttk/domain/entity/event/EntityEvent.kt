package com.github.caay2000.ttk.domain.entity.event

import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.infra.eventbus.event.Event
import com.github.caay2000.ttk.shared.EntityId
import java.util.UUID

sealed class EntityEvent(override val aggregateId: EntityId) : Event {
    override val eventId: UUID = UUID.randomUUID()
}

data class EntityUnloadedEvent(override val aggregateId: EntityId, val amount: Int, val position: Position) : EntityEvent(aggregateId)
data class EntityLoadedEvent(override val aggregateId: EntityId, val amount: Int, val position: Position) : EntityEvent(aggregateId)
