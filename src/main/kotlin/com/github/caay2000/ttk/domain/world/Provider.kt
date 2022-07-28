package com.github.caay2000.ttk.domain.world

import arrow.core.Either
import com.github.caay2000.ttk.domain.configuration.Configuration

interface Provider {

    fun get(): Either<ProviderException, World>
    fun set(world: World): Either<ProviderException, World>

    fun getConfiguration(): Either<ProviderException, Configuration>
    fun setConfiguration(configuration: Configuration): Either<ProviderException, Configuration>

    class ProviderException : RuntimeException {
        constructor(message: String) : super(message)
        constructor(cause: Throwable) : super(cause)
    }
}
