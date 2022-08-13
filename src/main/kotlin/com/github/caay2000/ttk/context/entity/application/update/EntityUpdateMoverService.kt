package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.application.EntityException
import com.github.caay2000.ttk.context.entity.application.EntityService
import com.github.caay2000.ttk.context.entity.application.EntityUpdateMoverServiceException
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.update.NextSectionStrategy

class EntityUpdateMoverService(provider: Provider, eventPublisher: EventPublisher<Event>) : EntityService(provider, eventPublisher) {

    private val nextSectionStrategy = NextSectionStrategy.SimpleNextSectionStrategy(provider)

    fun invoke(initialEntity: Entity): Either<EntityException, Entity> =
        Either.catch { initialEntity.updateMove(nextSectionStrategy) }
            .mapLeft { EntityUpdateMoverServiceException(it) }
}
