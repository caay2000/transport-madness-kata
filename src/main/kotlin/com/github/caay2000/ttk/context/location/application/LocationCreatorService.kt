package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.location.domain.ConfigurationNotFoundException
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.domain.LocationException
import com.github.caay2000.ttk.context.location.domain.LocationsTooCloseException
import com.github.caay2000.ttk.context.location.domain.UnknownLocationException
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World

class LocationCreatorService(provider: Provider, eventPublisher: EventPublisher<Event>) : LocationService(provider, eventPublisher) {

    fun invoke(name: String, position: Position, population: Int): Either<LocationException, World> =
        findWorld()
            .flatMap { world -> world.guardMinimumDistance(position) }
            .flatMap { world -> world.createLocation(name, position, population) }
            .flatMap { world -> world.save() }

    private fun findConfiguration(): Either<LocationException, Configuration> =
        provider.getConfiguration()
            .mapLeft { ConfigurationNotFoundException(it) }

    private fun World.guardMinimumDistance(position: Position) =
        findConfiguration()
            .flatMap { configuration ->
                if (anyLocationTooClose(position, configuration)) LocationsTooCloseException(position).left()
                else this.right()
            }

    private fun World.createLocation(name: String, position: Position, population: Int): Either<LocationException, World> =
        findConfiguration()
            .map { configuration -> Location.create(name = name, position = position, population = population, configuration = configuration) }
            .map { location -> addLocation(location) }

    private fun World.anyLocationTooClose(
        position: Position,
        configuration: Configuration
    ) = locations.values.any { it.position.distance(position) < configuration.minDistanceBetweenCities.toDouble() }

    private fun World.save(): Either<LocationException, World> =
        provider.set(this)
            .mapLeft { UnknownLocationException(it) }
}
