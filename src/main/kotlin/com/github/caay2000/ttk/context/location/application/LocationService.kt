package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.location.application.LocationRepository.FindLocationCriteria
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.domain.LocationException
import com.github.caay2000.ttk.context.location.domain.UnknownLocationException

interface LocationServiceApi {

    fun find(criteria: FindLocationCriteria): Either<LocationException, Location>
    fun save(location: Location): Either<LocationException, Location>
    fun publishEvents(location: Location): Either<LocationException, Location>
}

fun locationService(locationRepository: LocationRepository, eventPublisher: EventPublisher) = object : LocationServiceApi {

    private val locationFinder = LocationFinder(locationRepository)

    override fun find(criteria: FindLocationCriteria): Either<LocationException, Location> =
        locationFinder.invoke(criteria)

    override fun save(location: Location): Either<LocationException, Location> =
        locationRepository.save(location)
            .map { location }
            .mapLeft { UnknownLocationException(it) }

    override fun publishEvents(location: Location): Either<LocationException, Location> =
        Either.catch { eventPublisher.publish(location.pullEvents()).let { location } }
            .mapLeft { UnknownLocationException(it) }
}
