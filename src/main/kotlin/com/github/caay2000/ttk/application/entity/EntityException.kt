package com.github.caay2000.ttk.application.entity

import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.shared.EntityId

sealed class EntityException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

data class EntityNotFound(val entityId: EntityId) : EntityException("entity $entityId not found")
data class InvalidRouteException(val stops: List<Position>) : EntityException("invalid route for $stops")
data class InvalidEntityPositionException(val position: Position) : EntityException("invalid position $position")
data class UnknownEntityException(override val cause: Throwable) : EntityException(cause)
