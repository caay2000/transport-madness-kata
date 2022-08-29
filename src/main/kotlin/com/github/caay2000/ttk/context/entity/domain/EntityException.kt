package com.github.caay2000.ttk.context.entity.domain

import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.EntityId

sealed class EntityException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

data class EntityInvalidNumOfCoachesException(val amount: Int, val entityType: EntityType) :
    EntityException("${entityType.name} cannot have $amount num of coaches, max is ${entityType.maxNumCoaches}")

data class CompanyNotFoundEntityException(val companyId: CompanyId) : EntityException("companyId $companyId not found")
data class EntityNotFoundEntityException(val entityId: EntityId) : EntityException("entity $entityId not found")
data class InvalidRouteEntityException(val stops: List<Position>) : EntityException("invalid route for $stops")
data class InvalidEntityPositionEntityException(val position: Position) : EntityException("invalid position $position")
data class EntityUpdateLoaderServiceEntityException(override val cause: Throwable) : EntityException(cause)
data class EntityUpdateMoverServiceEntityException(override val cause: Throwable) : EntityException(cause)
data class EntityUpdateStarterServiceEntityException(override val cause: Throwable) : EntityException(cause)
data class EntityUpdateStopperServiceEntityException(override val cause: Throwable) : EntityException(cause)
data class EntityUpdateUnloaderServiceEntityException(override val cause: Throwable) : EntityException(cause)

data class UnknownEntityException(override val cause: Throwable) : EntityException(cause)
