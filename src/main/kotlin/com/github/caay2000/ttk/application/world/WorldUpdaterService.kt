package com.github.caay2000.ttk.application.world

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.flatMap
import com.github.caay2000.ttk.application.entity.EntityUpdaterService
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.domain.world.WorldProvider

class WorldUpdaterService(
    private val worldProvider: WorldProvider,
    private val entityUpdaterService: EntityUpdaterService = EntityUpdaterService(worldProvider)
) {

    fun invoke(): Either<WorldException, World> =
        findWorld()
            .flatMap { world -> world.doUpdate() }
            .flatMap { world -> world.save() }

    private fun findWorld(): Either<WorldException, World> =
        worldProvider.get()
            .mapLeft { UnknownWorldException(it) }

    private fun World.doUpdate(): Either<WorldException, World> =
        Either.catch { entities.keys.fold(this) { _, entityId -> entityUpdaterService.invoke(entityId).bind() } }
            .map { world -> world.update() }
            .mapLeft { UnknownWorldException(it) }

    private fun World.save(): Either<WorldException, World> =
        worldProvider.set(this)
            .mapLeft { UnknownWorldException(it) }
}
