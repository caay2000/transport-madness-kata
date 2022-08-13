package com.github.caay2000.ttk.infra.provider

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.api.provider.Provider.ProviderException
import com.github.caay2000.ttk.api.provider.Provider.ProviderException.ConfigurationNotFoundProviderException
import com.github.caay2000.ttk.api.provider.Provider.ProviderException.UnknownProviderException
import com.github.caay2000.ttk.api.provider.Provider.ProviderException.WorldNotFoundProviderException
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.world.domain.World

class DefaultProvider : Provider {

    private var world: World? = null
    private var configuration: Configuration? = null

    override fun get(): Either<ProviderException, World> =
        world.rightIfNotNull { WorldNotFoundProviderException }

    override fun set(world: World): Either<ProviderException, World> =
        Either.catch { this.world = world }
            .map { world }
            .mapLeft { UnknownProviderException(it) }

    override fun getConfiguration(): Either<ProviderException, Configuration> =
        configuration.rightIfNotNull { ConfigurationNotFoundProviderException }

    override fun setConfiguration(configuration: Configuration): Either<ProviderException, Configuration> =
        Either.catch { this.configuration = configuration }
            .map { configuration }
            .mapLeft { UnknownProviderException(it) }
}
