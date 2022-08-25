package com.github.caay2000.ttk.context.entity.application

import arrow.core.Either
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.application.EntityRepository.FindEntityCriteria.ByIdCriteria
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityNotFound
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException
import com.github.caay2000.ttk.shared.EntityId

abstract class EntityService(protected val entityRepository: EntityRepository, protected val eventPublisher: EventPublisher<Event>) {

    protected fun findEntity(entityId: EntityId): Either<EntityException, Entity> =
        entityRepository.find(ByIdCriteria(entityId))
            .mapLeft { EntityNotFound(entityId) }

    protected fun Entity.save(): Either<EntityException, Entity> =
        entityRepository.save(this)
            .map { this }
            .mapLeft { UnknownEntityException(it) }

    protected fun Entity.publishEvents(): Either<EntityException, Entity> =
        Either.catch { eventPublisher.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownEntityException(it) }
}
