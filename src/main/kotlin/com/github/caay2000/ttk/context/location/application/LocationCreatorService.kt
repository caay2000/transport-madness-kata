package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.configuration.application.ConfigurationRepository
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.location.application.LocationRepository.FindLocationCriteria.ByPositionCriteria
import com.github.caay2000.ttk.context.location.domain.ConfigurationNotFoundException
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.domain.LocationAlreadyExists
import com.github.caay2000.ttk.context.location.domain.LocationException
import com.github.caay2000.ttk.context.location.domain.LocationsTooCloseException
import com.github.caay2000.ttk.context.world.domain.Position

class LocationCreatorService(
    private val provider: Provider,
    private val configurationRepository: ConfigurationRepository,
    private val locationRepository: LocationRepository,
    eventPublisher: EventPublisher<Event>
) : LocationService(locationRepository, eventPublisher) {

    fun invoke(name: String, position: Position, population: Int): Either<LocationException, Location> =
        guardPositionEmpty(position)
            .flatMap { guardMinimumDistance(position) }
            .flatMap { createLocation(name, position, population) }
            .flatMap { location -> location.save() }
            .flatMap { location -> location.publishEvents() }

    private fun guardPositionEmpty(position: Position): Either<LocationException, Unit> =
        if (locationRepository.exists(ByPositionCriteria(position)))
            LocationAlreadyExists(position).left()
        else Unit.right()

    private fun guardMinimumDistance(position: Position): Either<LocationException, Unit> =
        findConfiguration()
            .flatMap { configuration -> anyLocationTooClose(position, configuration) }

    private fun createLocation(name: String, position: Position, population: Int): Either<LocationException, Location> =
        findConfiguration()
            .map { configuration -> Location.create(name = name, position = position, population = population, configuration = configuration) }

    private fun findConfiguration(): Either<LocationException, Configuration> =
        configurationRepository.get()
            .mapLeft { ConfigurationNotFoundException(it) }

    private fun anyLocationTooClose(position: Position, configuration: Configuration): Either<LocationException, Unit> =
        findAllLocations()
            .map { locations -> locations.any { it.position.distance(position) < configuration.minDistanceBetweenCities.toDouble() } }
            .flatMap { anyLocationTooClose -> if (anyLocationTooClose) LocationsTooCloseException(position).left() else Unit.right() }
}
