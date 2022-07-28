package com.github.caay2000.ttk.application.world

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.flatMap
import com.github.caay2000.ttk.application.entity.EntityUpdaterService
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World

class WorldUpdaterService(
    private val provider: Provider,
    private val entityUpdaterService: EntityUpdaterService = EntityUpdaterService(provider)
) {

    fun invoke(): Either<WorldException, World> =
        findWorld()
            .flatMap { world -> world.doUpdate() }
            .flatMap { world -> world.save() }

    private fun findWorld(): Either<WorldException, World> =
        provider.get()
            .mapLeft { UnknownWorldException(it) }

    private fun World.doUpdate(): Either<WorldException, World> =
        Either.catch { entities.keys.fold(this) { _, entityId -> entityUpdaterService.invoke(entityId).bind() } }
            .map { world -> world.update() }
            .mapLeft { UnknownWorldException(it) }

    private fun World.save(): Either<WorldException, World> =
        provider.set(this)
            .mapLeft { UnknownWorldException(it) }
}
