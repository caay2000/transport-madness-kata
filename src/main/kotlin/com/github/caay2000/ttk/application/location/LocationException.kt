package com.github.caay2000.ttk.application.location

import com.github.caay2000.ttk.domain.world.Position

sealed class LocationException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

data class LocationNotFound(val position: Position) : LocationException("location in position $position not found")
data class UnknownLocationException(override val cause: Throwable) : LocationException(cause)
