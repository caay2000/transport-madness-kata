package com.github.caay2000.ttk.context.configuration.application

import arrow.core.Either
import com.github.caay2000.ttk.context.configuration.domain.Configuration

class ConfigurationSetterService(private val configurationRepository: ConfigurationRepository) {

    fun invoke(configuration: Configuration): Either<ConfigurationException, Configuration> =
        configurationRepository.save(configuration)
            .mapLeft { UnknownConfigurationException(it) }
}
