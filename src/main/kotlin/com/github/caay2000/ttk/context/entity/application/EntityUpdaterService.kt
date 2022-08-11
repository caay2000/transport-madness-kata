package com.github.caay2000.ttk.context.entity.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.shared.EntityId

class EntityUpdaterService(
    provider: Provider,
    eventPublisher: EventPublisher<Event>,
    queryExecutor: QueryExecutor
) : EntityService(provider, eventPublisher) {

    private val entityUpdaterServiceOperations = EntityUpdateServiceOperations(queryExecutor)

    fun invoke(entityId: EntityId): Either<EntityException, Entity> =
        findEntity(entityId)
            .flatMap { entity -> entity.updateEntity() }
            .flatMap { entity -> entity.save() }
            .flatMap { entity -> entity.publishEvents() }

    private fun Entity.updateEntity(): Either<EntityException, Entity> =
        Either.catch { update(entityUpdaterServiceOperations) }
            .mapLeft { UnknownEntityException(it) }
}
