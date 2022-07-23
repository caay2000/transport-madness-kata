package com.github.caay2000.ttk.application.entity

import arrow.core.Either
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.domain.world.WorldProvider
import com.github.caay2000.ttk.shared.EntityId

abstract class EntityService(protected val worldProvider: WorldProvider) {

    protected fun findWorld(): Either<EntityException, World> =
        worldProvider.get()
            .mapLeft { UnknownEntityException(it) }

    protected fun World.findEntity(entityId: EntityId): Either<EntityException, Entity> =
        Either.catch { getEntity(entityId) }
            .mapLeft { EntityNotFound(entityId) }

    protected fun World.save(): Either<EntityException, World> =
        worldProvider.set(this)
            .mapLeft { UnknownEntityException(it) }
}
