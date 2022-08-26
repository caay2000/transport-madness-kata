package com.github.caay2000.ttk.context.configuration.application

import arrow.core.Either
import arrow.core.right
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.location.domain.LocationConfiguration
import com.github.caay2000.ttk.context.world.domain.WorldConfiguration

class ConfigurationSetterService {

    fun invoke(configuration: Configuration): Either<ConfigurationException, Unit> {
        WorldConfiguration.fromConfiguration(configuration)
        LocationConfiguration.fromConfiguration(configuration)
        return Unit.right()
    }
}
