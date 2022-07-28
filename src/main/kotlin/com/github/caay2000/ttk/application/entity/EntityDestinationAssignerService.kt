package com.github.caay2000.ttk.application.entity

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.domain.world.WorldProvider
import com.github.caay2000.ttk.shared.EntityId

class EntityDestinationAssignerService(worldProvider: WorldProvider) : EntityService(worldProvider) {

    fun invoke(entityId: EntityId, position: Position): Either<EntityException, World> =
        findWorld()
            .flatMap { world -> world.updateEntityDestination(entityId, position) }
            .flatMap { world -> world.save() }

    private fun World.updateEntityDestination(entityId: EntityId, position: Position): Either<EntityException, World> =
        findEntity(entityId)
            .map { entity -> entity.setDestination(position) }
            .map { entity -> putEntity(entity) }
            .mapLeft { UnknownEntityException(it) }
}
