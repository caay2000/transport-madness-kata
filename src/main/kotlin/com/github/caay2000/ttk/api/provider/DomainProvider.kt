package com.github.caay2000.ttk.api.provider

import arrow.core.Either
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.world.domain.World

object DomainProvider : Provider {

    private lateinit var provider: Provider

    fun init(provider: Provider) {
        DomainProvider.provider = provider
    }

    override fun get(): Either<Provider.ProviderException, World> = provider.get()
    override fun set(world: World): Either<Provider.ProviderException, World> = provider.set(world)
    override fun getConfiguration(): Either<Provider.ProviderException, Configuration> = provider.getConfiguration()
    override fun setConfiguration(configuration: Configuration): Either<Provider.ProviderException, Configuration> = provider.setConfiguration(configuration)
}
