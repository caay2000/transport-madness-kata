package com.github.caay2000.ttk.context.world.domain

sealed class WorldException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

data class LocationsTooCloseException(override val message: String) : WorldException(message)
data class WorldNotFoundWorldException(override val cause: Throwable) : WorldException(cause)
data class ConfigurationNotFoundWorldException(override val cause: Throwable) : WorldException(cause)
data class EntitiesUpdateWorldException(override val cause: Throwable) : WorldException(cause)
data class UnknownWorldException(override val cause: Throwable) : WorldException(cause)
