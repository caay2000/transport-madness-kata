package com.github.caay2000.ttk.application.world

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.domain.world.WorldProvider

class WorldCreatorService(private val worldProvider: WorldProvider) {

    fun invoke(configuration: Configuration): Either<WorldException, World> =
        createWorld(configuration)
            .flatMap { world -> world.save() }

    private fun createWorld(configuration: Configuration): Either<WorldException, World> =
        Either.catch { World.create(configuration.worldWidth, configuration.worldHeight) }
            .mapLeft { UnknownWorldException(it) }

    private fun World.save(): Either<WorldException, World> =
        worldProvider.set(this)
            .mapLeft { UnknownWorldException(it) }
}
