package com.github.caay2000.ttk.infra.eventbus.event

import com.github.caay2000.ttk.shared.DomainId
import java.util.UUID

interface Event {

    val eventId: UUID
    val aggregateId: DomainId
    fun type(): String = this::class.java.simpleName
}
