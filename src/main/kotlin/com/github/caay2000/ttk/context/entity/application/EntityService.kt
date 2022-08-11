package com.github.caay2000.ttk.context.entity.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.shared.EntityId

abstract class EntityService(protected val provider: Provider, protected val eventPublisher: EventPublisher<Event>) {

    protected fun findEntity(entityId: EntityId): Either<EntityException, Entity> =
        provider.get()
            .flatMap { world -> world.findEntity(entityId) }
            .mapLeft { UnknownEntityException(it) }

    protected fun findWorld(): Either<EntityException, World> =
        provider.get()
            .mapLeft { UnknownEntityException(it) }

    protected fun World.findEntity(entityId: EntityId): Either<EntityException, Entity> =
        Either.catch { getEntity(entityId) }
            .mapLeft { EntityNotFound(entityId) }

    protected fun Entity.save(): Either<EntityException, Entity> =
        provider.get()
            .map { world -> world.putEntity(this) }
            .flatMap { world -> provider.set(world) }
            .map { this }
            .mapLeft { UnknownEntityException(it) }

    protected fun Entity.publishEvents(): Either<EntityException, Entity> =
        Either.catch { eventPublisher.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownEntityException(it) }
}
