package com.github.caay2000.ttk.mock

import arrow.core.Either
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.infra.provider.DefaultProvider

object ProviderMock : Provider {

    private val provider = DefaultProvider()

    override fun get(): Either<Provider.ProviderException, World> = provider.get()
    override fun set(world: World): Either<Provider.ProviderException, World> = provider.set(world)
    override fun getConfiguration(): Either<Provider.ProviderException, Configuration> = provider.getConfiguration()
    override fun setConfiguration(configuration: Configuration): Either<Provider.ProviderException, Configuration> = provider.setConfiguration(configuration)
}
