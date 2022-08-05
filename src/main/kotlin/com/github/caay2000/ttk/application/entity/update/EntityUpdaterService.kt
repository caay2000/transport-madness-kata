package com.github.caay2000.ttk.application.entity.update

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.application.entity.EntityException
import com.github.caay2000.ttk.application.entity.EntityService
import com.github.caay2000.ttk.application.entity.UnknownEntityException
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.pathifinding.NextSectionFinder
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.infra.eventbus.event.Event
import com.github.caay2000.ttk.infra.eventbus.event.EventPublisher
import com.github.caay2000.ttk.shared.EntityId

class EntityUpdaterService(provider: Provider, eventPublisher: EventPublisher<Event>) : EntityService(provider, eventPublisher) {

    private val nextSectionFinder = NextSectionFinder(provider)

    fun invoke(entityId: EntityId): Either<EntityException, Entity> =
        findEntity(entityId)
            .flatMap { entity -> entity.updateEntity() }
            .flatMap { entity -> entity.save() }
            .flatMap { entity -> entity.publishEvents() }
            .mapLeft { UnknownEntityException(it) }

    private fun Entity.updateEntity(): Either<EntityException, Entity> =
        Either.catch { update(nextSectionFinder) }
            .mapLeft { UnknownEntityException(it) }
}
