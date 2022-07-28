package com.github.caay2000.ttk.application.entity

import arrow.core.Either
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.shared.EntityId

abstract class EntityService(protected val provider: Provider) {

    protected fun findWorld(): Either<EntityException, World> =
        provider.get()
            .mapLeft { UnknownEntityException(it) }

    protected fun World.findEntity(entityId: EntityId): Either<EntityException, Entity> =
        Either.catch { getEntity(entityId) }
            .mapLeft { EntityNotFound(entityId) }

    protected fun World.save(): Either<EntityException, World> =
        provider.set(this)
            .mapLeft { UnknownEntityException(it) }
}
