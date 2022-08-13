package com.github.caay2000.ttk.context.entity.application

import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.shared.EntityId

sealed class EntityException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

data class EntityNotFound(val entityId: EntityId) : EntityException("entity $entityId not found")
data class InvalidRouteException(val stops: List<Position>) : EntityException("invalid route for $stops")
data class InvalidEntityPositionException(val position: Position) : EntityException("invalid position $position")
data class InvalidEntityNextSectionException(val source: Position, val target: Position, override val cause: Throwable) :
    EntityException("error finding next section from $source to $target: ${cause.message}")

data class EntityUpdateLoaderServiceException(override val cause: Throwable) : EntityException(cause)
data class EntityUpdateMoverServiceException(override val cause: Throwable) : EntityException(cause)
data class EntityUpdateStarterServiceException(override val cause: Throwable) : EntityException(cause)
data class EntityUpdateStopperServiceException(override val cause: Throwable) : EntityException(cause)
data class EntityUpdateUnloaderServiceException(override val cause: Throwable) : EntityException(cause)

data class UnknownEntityException(override val cause: Throwable) : EntityException(cause)
