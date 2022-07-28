package com.github.caay2000.ttk.domain.world

import arrow.core.Either

interface WorldProvider {

    fun get(): Either<ProviderException, World>
    fun set(world: World): Either<ProviderException, World>

    class ProviderException : RuntimeException {
        constructor(message: String) : super(message)
        constructor(cause: Throwable) : super(cause)
    }
}
