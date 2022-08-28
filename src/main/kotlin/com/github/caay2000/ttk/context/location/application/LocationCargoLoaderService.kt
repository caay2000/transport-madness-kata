package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.location.application.LocationRepository.FindLocationCriteria.ByPositionCriteria
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.domain.LocationException
import com.github.caay2000.ttk.context.location.domain.UnknownLocationException
import com.github.caay2000.ttk.context.world.domain.Position

class LocationCargoLoaderService(
    private val locationRepository: LocationRepository,
    private val eventPublisher: EventPublisher
) {

    private val locationFinder = LocationFinder(locationRepository)

    fun invoke(position: Position, amountLoaded: Int): Either<LocationException, Location> =
        findLocation(position)
            .map { location -> location.load(amountLoaded) }
            .flatMap { location -> location.save() }
            .flatMap { location -> location.publishEvents() }

    private fun findLocation(position: Position): Either<LocationException, Location> =
        locationFinder.invoke(ByPositionCriteria(position))

    private fun Location.save(): Either<LocationException, Location> =
        locationRepository.save(this)
            .mapLeft { UnknownLocationException(it) }

    private fun Location.publishEvents(): Either<LocationException, Location> =
        Either.catch { eventPublisher.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownLocationException(it) }
}
