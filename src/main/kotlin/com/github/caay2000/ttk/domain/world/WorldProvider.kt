package com.github.caay2000.ttk.domain.world

import arrow.core.Either

interface WorldProvider {

    fun get(): Either<Throwable, World>
    fun set(world: World): Either<Throwable, World>
}
