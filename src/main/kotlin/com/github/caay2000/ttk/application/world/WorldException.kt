package com.github.caay2000.ttk.application.world

sealed class WorldException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

data class UnknownWorldException(override val cause: Throwable) : WorldException(cause)
