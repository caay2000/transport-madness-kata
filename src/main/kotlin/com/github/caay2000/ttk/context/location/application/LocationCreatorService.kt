package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.location.application.LocationRepository.FindLocationCriteria.ByPositionCriteria
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.domain.LocationAlreadyExists
import com.github.caay2000.ttk.context.location.domain.LocationConfiguration
import com.github.caay2000.ttk.context.location.domain.LocationException
import com.github.caay2000.ttk.context.location.domain.LocationsTooCloseException
import com.github.caay2000.ttk.context.location.domain.UnknownLocationException
import com.github.caay2000.ttk.context.world.domain.Position

class LocationCreatorService(
    private val locationRepository: LocationRepository,
    private val eventPublisher: EventPublisher
) {
    private val locationService: LocationServiceApi = locationService(locationRepository, eventPublisher)

    fun invoke(name: String, position: Position, population: Int): Either<LocationException, Location> =
        guardPositionEmpty(position)
            .flatMap { guardMinimumDistance(position) }
            .flatMap { createLocation(name, position, population) }
            .flatMap { location -> locationService.save(location) }
            .flatMap { location -> locationService.publishEvents(location) }

    private fun guardPositionEmpty(position: Position): Either<LocationException, Unit> =
        if (locationRepository.exists(ByPositionCriteria(position)))
            LocationAlreadyExists(position).left()
        else Unit.right()

    private fun createLocation(name: String, position: Position, population: Int): Either<LocationException, Location> =
        Either.catch { Location.create(name = name, position = position, population = population) }
            .mapLeft { UnknownLocationException(it) }

    private fun guardMinimumDistance(position: Position): Either<LocationException, Unit> =
        findAllLocations()
            .map { locations -> locations.any { it.position.distance(position) < LocationConfiguration.get().minDistanceBetweenCities } }
            .flatMap { anyLocationTooClose -> if (anyLocationTooClose) LocationsTooCloseException(position).left() else Unit.right() }

    private fun findAllLocations(): Either<LocationException, List<Location>> =
        locationRepository.findAll()
            .mapLeft { UnknownLocationException(it) }
}
