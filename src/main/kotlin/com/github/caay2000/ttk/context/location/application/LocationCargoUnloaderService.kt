package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.domain.LocationException
import com.github.caay2000.ttk.context.world.domain.Position

class LocationCargoUnloaderService(locationRepository: LocationRepository, eventPublisher: EventPublisher<Event>) : LocationService(locationRepository, eventPublisher) {

    fun invoke(position: Position, amountUnloaded: Int): Either<LocationException, Location> =
        findLocation(position)
            .map { location -> location.unload(amountUnloaded) }
            .flatMap { location -> location.save() }
            .flatMap { location -> location.publishEvents() }
}
