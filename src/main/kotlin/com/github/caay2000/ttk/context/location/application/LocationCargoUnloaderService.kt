package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.location.application.LocationRepository.FindLocationCriteria.ByPosition
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.domain.LocationException
import com.github.caay2000.ttk.context.world.domain.Position

class LocationCargoUnloaderService(
    private val locationRepository: LocationRepository,
    private val eventPublisher: EventPublisher
) {

    private val locationService: LocationServiceApi = locationService(locationRepository, eventPublisher)

    fun invoke(position: Position, amountUnloaded: Int): Either<LocationException, Location> =
        locationService.find(ByPosition(position))
            .map { location -> location.unload(amountUnloaded) }
            .flatMap { location -> locationService.save(location) }
            .flatMap { location -> locationService.publishEvents(location) }
}
