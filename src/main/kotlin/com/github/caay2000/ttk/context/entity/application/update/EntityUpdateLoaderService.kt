package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityUpdateLoaderServiceException
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException
import com.github.caay2000.ttk.context.entity.domain.update.LoadPassengersStrategy

class EntityUpdateLoaderService(
    queryExecutor: QueryExecutor,
    private val eventPublisher: EventPublisher
) {

    private val loadPassengersStrategy = LoadPassengersStrategy.SimpleLoadPassengersStrategy(queryExecutor)

    fun invoke(entity: Entity): Either<EntityException, Entity> =
        Either.catch { entity.updateLoad(loadPassengersStrategy) }
            .mapLeft { EntityUpdateLoaderServiceException(it) }
            .flatMap { updatedEntity -> updatedEntity.publishEvents() }

    private fun Entity.publishEvents(): Either<EntityException, Entity> =
        Either.catch { eventPublisher.publish(pullEvents()).let { this } }
            .mapLeft { UnknownEntityException(it) }
}
