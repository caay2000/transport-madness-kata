package com.github.caay2000.ttk.context.configuration.application

import arrow.core.Either
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.configuration.domain.Configuration

class ConfigurationFinder(private val provider: Provider) {

    fun invoke(): Either<ConfigurationException, Configuration> =
        provider.getConfiguration()
            .mapLeft { ConfigurationNotFoundException }
}
