package com.github.caay2000.ttk.application.entity

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.shared.EntityId

class EntityUpdaterService(provider: Provider) : EntityService(provider) {

    fun invoke(entityId: EntityId): Either<EntityException, World> =
        findWorld()
            .flatMap { world -> world.updateEntity(entityId) }
            .flatMap { world -> world.save() }

    private fun World.updateEntity(entityId: EntityId): Either<EntityException, World> =
        findEntity(entityId)
            .map { entity -> entity.update() }
            .map { entity -> putEntity(entity) }
            .mapLeft { UnknownEntityException(it) }
}
