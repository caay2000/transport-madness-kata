package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position

class LocationCargoLoaderService(provider: Provider, eventPublisher: EventPublisher<Event>) : LocationService(provider, eventPublisher) {

    fun invoke(position: Position, amountLoaded: Int): Either<LocationException, Location> =
        findWorld()
            .flatMap { world -> world.findLocation(position) }
            .map { location -> location.load(amountLoaded) }
            .flatMap { location -> location.save() }
            .flatMap { location -> location.publishEvents() }
}
