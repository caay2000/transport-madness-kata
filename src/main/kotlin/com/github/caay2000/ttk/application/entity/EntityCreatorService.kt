package com.github.caay2000.ttk.application.entity

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World

class EntityCreatorService(provider: Provider) : EntityService(provider) {

    fun invoke(position: Position): Either<EntityException, World> =
        findWorld()
            .flatMap { world -> world.guardPosition(position) }
            .flatMap { world -> world.createEntity(position) }
            .flatMap { world -> world.save() }

    private fun World.createEntity(position: Position): Either<EntityException, World> =
        provider.getConfiguration()
            .map { configuration -> putEntity(Entity.create(position = position, configuration = configuration)) }
            .mapLeft { UnknownEntityException(it) }

    private fun World.guardPosition(position: Position): Either<EntityException, World> =
        Either.catch { getCell(position) }
            .map { this }
            .mapLeft { InvalidEntityPositionException(position) }
}
