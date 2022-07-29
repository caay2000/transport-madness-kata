package com.github.caay2000.ttk.application.world

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World

class WorldCreatorService(provider: Provider) : WorldService(provider) {

    fun invoke(): Either<WorldException, World> =
        findConfiguration()
            .flatMap { configuration -> createWorld(configuration) }
            .flatMap { world -> world.save() }

    private fun createWorld(configuration: Configuration): Either<WorldException, World> =
        Either.catch { World.create(configuration.worldWidth, configuration.worldHeight) }
            .mapLeft { UnknownWorldException(it) }
}
