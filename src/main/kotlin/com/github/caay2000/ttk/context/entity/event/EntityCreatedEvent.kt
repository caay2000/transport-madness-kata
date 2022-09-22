package com.github.caay2000.ttk.context.entity.event

import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.context.entity.domain.EntityType
import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.EntityId
import java.util.UUID

data class EntityCreatedEvent(
    override val aggregateId: EntityId,
    val companyId: CompanyId,
    val entityType: EntityType
) : Event {
    override val eventId: UUID = UUID.randomUUID()
}
