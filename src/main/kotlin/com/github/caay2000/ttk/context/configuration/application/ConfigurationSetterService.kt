package com.github.caay2000.ttk.context.configuration.application

import arrow.core.Either
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.configuration.domain.Configuration

class ConfigurationSetterService(private val provider: Provider) {

    fun invoke(configuration: Configuration): Either<ConfigurationException, Configuration> =
        provider.setConfiguration(configuration)
            .mapLeft { UnknownConfigurationException(it) }
}
