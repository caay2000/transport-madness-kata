package com.github.caay2000.ttk.infra.provider

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.github.caay2000.ttk.api.provider.DomainProvider
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.api.provider.Provider.ProviderException
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.world.domain.World

class DefaultProvider : Provider {

    init {
        DomainProvider.init(this)
    }

    private var world: World? = null
    private var configuration: Configuration? = null

    override fun get(): Either<ProviderException, World> =
        world.rightIfNotNull { ProviderException("World not found") }

    override fun set(world: World): Either<ProviderException, World> =
        Either.catch { this.world = world }
            .map { world }
            .mapLeft { ProviderException(it) }

    override fun getConfiguration(): Either<ProviderException, Configuration> =
        configuration.rightIfNotNull { ProviderException("Configuration not found") }

    override fun setConfiguration(configuration: Configuration): Either<ProviderException, Configuration> =
        Either.catch { this.configuration = configuration }
            .map { configuration }
            .mapLeft { ProviderException(it) }
}
