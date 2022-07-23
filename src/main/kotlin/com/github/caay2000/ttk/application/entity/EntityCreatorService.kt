package com.github.caay2000.ttk.application.entity

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.domain.world.WorldProvider

class EntityCreatorService(worldProvider: WorldProvider) : EntityService(worldProvider) {

    fun invoke(position: Position): Either<EntityException, World> =
        findWorld()
            .flatMap { world -> world.guardPosition(position) }
            .flatMap { world -> world.createEntity(position) }
            .flatMap { world -> world.save() }

    private fun World.createEntity(position: Position): Either<EntityException, World> =
        Either.catch { this.putEntity(Entity.create(position = position)) }
            .mapLeft { UnknownEntityException(it) }

    private fun World.guardPosition(position: Position): Either<EntityException, World> =
        Either.catch { this.getCell(position) }
            .map { this }
            .mapLeft { InvalidEntityPositionException(position) }
}
