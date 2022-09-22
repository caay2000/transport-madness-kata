package com.github.caay2000.ttk.api.event

interface QueryResponse {

    val value: Any
    val type: String
        get() = this::class.java.simpleName
}
