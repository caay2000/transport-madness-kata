package com.github.caay2000.ttk.domain.entity.event

import com.github.caay2000.ttk.infra.eventbus.event.Event
import com.github.caay2000.ttk.shared.DomainId
import java.util.UUID

data class EntityEvent(override val aggregateId: DomainId) : Event {
    override val eventId: UUID = UUID.randomUUID()
}
