package com.github.caay2000.ttk.context.location.application

import com.github.caay2000.ttk.context.world.domain.Position

sealed class LocationException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

data class LocationNotFound(val position: Position) : LocationException("location in position $position not found")
data class UnknownLocationException(override val cause: Throwable) : LocationException(cause)
