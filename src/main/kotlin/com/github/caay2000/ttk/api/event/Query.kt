package com.github.caay2000.ttk.api.event

import java.util.UUID

interface Query {

    val queryId: UUID
    fun type(): String = this::class.java.simpleName
}

interface QueryResponse {

    val queryId: UUID
    fun type(): String = this::class.java.simpleName
    val value: Any
}
