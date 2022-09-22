package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.WorldException
import com.github.caay2000.ttk.context.world.domain.WorldNotFoundWorldException

class WorldFinderService(private val worldRepository: WorldRepository) {

    fun invoke(): Either<WorldException, World> =
        worldRepository.get()
            .mapLeft { WorldNotFoundWorldException(it) }
}
