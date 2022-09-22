package com.github.caay2000.ttk.api.event

import java.util.UUID

interface Query {

    val queryId: UUID
    val type: String
        get() = this::class.java.simpleName
}
