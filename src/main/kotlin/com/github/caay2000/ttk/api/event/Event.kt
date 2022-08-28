package com.github.caay2000.ttk.api.event

import com.github.caay2000.ttk.shared.DomainId
import java.util.UUID

interface Event {

    val eventId: UUID
    val type: String
        get() = this::class.java.simpleName

    val aggregateId: DomainId
}
