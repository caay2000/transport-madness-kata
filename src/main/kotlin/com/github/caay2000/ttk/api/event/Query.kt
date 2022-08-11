package com.github.caay2000.ttk.api.event

import java.util.UUID

interface Query {

    val queryId: UUID
    fun type(): String = this::class.java.simpleName

    interface Response {

        val queryId: UUID
        fun type(): String = this::class.java.simpleName
    }
}
