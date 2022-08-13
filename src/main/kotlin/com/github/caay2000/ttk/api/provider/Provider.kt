package com.github.caay2000.ttk.api.provider

import arrow.core.Either
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.world.domain.World

interface Provider {

    fun get(): Either<ProviderException, World>
    fun set(world: World): Either<ProviderException, World>

    fun getConfiguration(): Either<ProviderException, Configuration>
    fun setConfiguration(configuration: Configuration): Either<ProviderException, Configuration>

    sealed class ProviderException : RuntimeException {
        constructor() : super()
        constructor(cause: Throwable) : super(cause)

        object WorldNotFoundProviderException : ProviderException()
        object ConfigurationNotFoundProviderException : ProviderException()
        data class UnknownProviderException(override val cause: Throwable) : ProviderException(cause)
    }
}
