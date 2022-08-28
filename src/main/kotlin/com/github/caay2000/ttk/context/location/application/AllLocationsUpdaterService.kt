package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.domain.LocationException
import com.github.caay2000.ttk.context.location.domain.UnknownLocationException

class AllLocationsUpdaterService(
    private val locationRepository: LocationRepository,
    private val eventPublisher: EventPublisher
) {

    fun invoke(): Either<LocationException, Unit> =
        findAllLocations()
            .flatMap { locations -> locations.updateAll() }
            .void()

    private fun List<Location>.updateAll(): Either<LocationException, Unit> =
        Either.catch { forEach { it.updateLocation().bind() } }
            .mapLeft { UnknownLocationException(it) }

    private fun Location.updateLocation(): Either<LocationException, Location> =
        update().right()
            .flatMap { location -> location.save() }
            .flatMap { location -> location.publishEvents() }

    private fun findAllLocations(): Either<LocationException, List<Location>> =
        locationRepository.findAll()
            .mapLeft { UnknownLocationException(it) }

    private fun Location.save(): Either<LocationException, Location> =
        locationRepository.save(this)
            .mapLeft { UnknownLocationException(it) }

    private fun Location.publishEvents(): Either<LocationException, Location> =
        Either.catch { eventPublisher.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownLocationException(it) }
}
