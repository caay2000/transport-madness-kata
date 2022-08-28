package com.github.caay2000.ttk.api.event

import java.util.UUID

interface Command {

    val commandId: UUID
    val type: String
        get() = this::class.java.simpleName
}
