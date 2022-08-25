package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.application.EntityRepository
import com.github.caay2000.ttk.context.entity.application.EntityService
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityUpdateStopperServiceException

class EntityUpdateStopperService(entityRepository: EntityRepository, eventPublisher: EventPublisher<Event>) : EntityService(entityRepository, eventPublisher) {

    fun invoke(initialEntity: Entity): Either<EntityException, Entity> =
        Either.catch { initialEntity.updateStop() }
            .mapLeft { EntityUpdateStopperServiceException(it) }
            .flatMap { entity -> entity.publishEvents() }
}
