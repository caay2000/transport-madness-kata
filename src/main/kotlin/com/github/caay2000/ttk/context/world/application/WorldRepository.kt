package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import com.github.caay2000.ttk.context.world.domain.World

interface WorldRepository {

    fun get(): Either<Throwable, World>
    fun save(world: World): Either<Throwable, World>
}
