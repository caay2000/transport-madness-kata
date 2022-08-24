package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.domain.LocationException
import com.github.caay2000.ttk.context.location.domain.UnknownLocationException

class LocationUpdaterService(locationRepository: LocationRepository, eventPublisher: EventPublisher<Event>) : LocationService(locationRepository, eventPublisher) {

    fun invoke(): Either<LocationException, Unit> =
        findAllLocations()
            .map { entities -> entities.updateAll() }

    private fun List<Location>.updateAll(): Either<LocationException, Unit> =
        Either.catch { forEach { it.updateLocation().bind() } }
            .mapLeft { UnknownLocationException(it) }

    private fun Location.updateLocation(): Either<LocationException, Location> =
        update().right()
            .flatMap { location -> location.save() }
}
