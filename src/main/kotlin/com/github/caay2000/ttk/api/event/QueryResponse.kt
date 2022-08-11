package com.github.caay2000.ttk.api.event

import java.util.UUID

interface QueryResponse {

    val queryId: UUID
    fun type(): String = this::class.java.simpleName
}
