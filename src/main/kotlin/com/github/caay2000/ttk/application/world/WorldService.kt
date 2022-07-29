package com.github.caay2000.ttk.application.world

import arrow.core.Either
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World

abstract class WorldService(protected val provider: Provider) {

    protected fun findWorld(): Either<WorldException, World> =
        provider.get()
            .mapLeft { UnknownWorldException(it) }

    protected fun findConfiguration(): Either<WorldException, Configuration> =
        provider.getConfiguration()
            .mapLeft { UnknownWorldException(it) }

    protected fun World.save(): Either<WorldException, World> =
        provider.set(this)
            .mapLeft { UnknownWorldException(it) }
}
