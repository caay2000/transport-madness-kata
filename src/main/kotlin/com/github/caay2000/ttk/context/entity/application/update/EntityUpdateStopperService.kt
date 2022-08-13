package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.application.EntityException
import com.github.caay2000.ttk.context.entity.application.EntityService
import com.github.caay2000.ttk.context.entity.application.EntityUpdateStopperServiceException
import com.github.caay2000.ttk.context.entity.domain.Entity

class EntityUpdateStopperService(provider: Provider, eventPublisher: EventPublisher<Event>) : EntityService(provider, eventPublisher) {

    fun invoke(initialEntity: Entity): Either<EntityException, Entity> =
        Either.catch { initialEntity.updateStop() }
            .mapLeft { EntityUpdateStopperServiceException(it) }
            .flatMap { entity -> entity.publishEvents() }
}
