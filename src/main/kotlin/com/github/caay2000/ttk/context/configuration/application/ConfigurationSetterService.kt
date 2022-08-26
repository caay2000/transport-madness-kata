package com.github.caay2000.ttk.context.configuration.application

import arrow.core.Either
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.location.domain.LocationConfiguration
import com.github.caay2000.ttk.context.world.domain.WorldConfiguration
import com.github.caay2000.ttk.pathfinding.PathfindingConfiguration

class ConfigurationSetterService {

    fun invoke(configuration: Configuration): Either<ConfigurationException, Unit> =
        Either.catch {
            WorldConfiguration.fromConfiguration(configuration)
            LocationConfiguration.fromConfiguration(configuration)
            PathfindingConfiguration.fromConfiguration(configuration)
        }.mapLeft { UnknownConfigurationException(it) }
            .void()
}
