package com.github.caay2000.ttk.application.entity

import com.github.caay2000.ttk.domain.world.Position

sealed class EntityException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

data class InvalidEntityPositionException(val position: Position) : EntityException("invalid position $position")
data class UnknownEntityException(override val cause: Throwable) : EntityException(cause)
