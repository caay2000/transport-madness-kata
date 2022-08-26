package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.domain.LocationConfiguration
import com.github.caay2000.ttk.context.location.domain.LocationException
import com.github.caay2000.ttk.context.location.domain.LocationNotFoundByPositionException
import com.github.caay2000.ttk.context.location.domain.UnknownLocationException
import com.github.caay2000.ttk.context.world.domain.Position

abstract class LocationService(private val locationRepository: LocationRepository, protected val eventPublisher: EventPublisher<Event>) {

    protected val configuration: LocationConfiguration
        get() = LocationConfiguration.get()

    protected fun findLocation(position: Position): Either<LocationException, Location> =
        locationRepository.find(LocationRepository.FindLocationCriteria.ByPositionCriteria(position))
            .mapLeft { LocationNotFoundByPositionException(position) }

    protected fun findAllLocations(): Either<LocationException, List<Location>> =
        locationRepository.findAll()
            .mapLeft { UnknownLocationException(it) }

    protected fun Location.save(): Either<LocationException, Location> =
        locationRepository.save(this)
            .mapLeft { UnknownLocationException(it) }

    protected fun Location.publishEvents(): Either<LocationException, Location> =
        Either.catch { eventPublisher.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownLocationException(it) }
}
