package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityUpdateStopperServiceException
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException

class EntityUpdateStopperService(private val eventPublisher: EventPublisher) {

    fun invoke(initialEntity: Entity): Either<EntityException, Entity> =
        Either.catch { initialEntity.updateStop() }
            .mapLeft { EntityUpdateStopperServiceException(it) }
            .flatMap { entity -> entity.publishEvents() }

    private fun Entity.publishEvents(): Either<EntityException, Entity> =
        Either.catch { eventPublisher.publish(pullEvents()).let { this } }
            .mapLeft { UnknownEntityException(it) }
}
