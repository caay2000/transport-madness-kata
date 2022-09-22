package com.github.caay2000.ttk.mother

import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.event.EntityCreatedEvent

object EntityCreatedEventMother {

    fun from(entity: Entity) =
        EntityCreatedEvent(
            aggregateId = entity.id,
            companyId = entity.companyId,
            entityType = entity.entityType
        )
}
