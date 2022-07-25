package com.github.caay2000.ttk.application.world

import arrow.core.Either
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.shared.EntityId

abstract class WorldService(protected val provider: Provider) {

    protected fun findWorld(): Either<WorldException, World> =
        provider.get()
            .mapLeft { UnknownWorldException(it) }

    protected fun World.save(): Either<WorldException, World> =
        provider.set(this)
            .mapLeft { UnknownWorldException(it) }
}
