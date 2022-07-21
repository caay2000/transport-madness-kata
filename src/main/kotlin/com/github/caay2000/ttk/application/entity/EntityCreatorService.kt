package com.github.caay2000.ttk.application.entity

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.domain.world.WorldProvider

class EntityCreatorService(private val worldProvider: WorldProvider) {

    fun invoke(position: Position): Either<EntityException, World> =
        findWorld()
            .flatMap { world -> world.guardPosition(position) }
            .flatMap { world -> world.createEntity(position) }
            .flatMap { world -> world.save() }

    private fun findWorld(): Either<EntityException, World> =
        worldProvider.get()
            .mapLeft { UnknownEntityException(it) }

    private fun World.createEntity(position: Position): Either<EntityException, World> =
        Either.catch { this.addEntity(Entity(position)) }
            .mapLeft { UnknownEntityException(it) }

    private fun World.guardPosition(position: Position): Either<EntityException, World> =
        Either.catch { this.getCell(position) }
            .map { this }
            .mapLeft { InvalidEntityPositionException(position) }

    private fun World.save(): Either<EntityException, World> =
        worldProvider.set(this)
            .mapLeft { UnknownEntityException(it) }
}
