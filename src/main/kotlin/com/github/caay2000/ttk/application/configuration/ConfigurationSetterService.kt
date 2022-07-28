package com.github.caay2000.ttk.application.configuration

import arrow.core.Either
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.world.Provider

class ConfigurationSetterService(private val provider: Provider) {

    fun invoke(configuration: Configuration): Either<ConfigurationException, Configuration> =
        provider.setConfiguration(configuration)
            .mapLeft { UnknownConfigurationException(it) }
}
