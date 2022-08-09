package com.github.caay2000.ttk.application.location

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.domain.location.Location
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.infra.eventbus.event.Event
import com.github.caay2000.ttk.infra.eventbus.event.EventPublisher

class LocationCargoLoaderService(provider: Provider, eventPublisher: EventPublisher<Event>) : LocationService(provider, eventPublisher) {

    fun invoke(position: Position, amountLoaded: Int): Either<LocationException, Location> =
        findWorld()
            .flatMap { world -> world.findLocation(position) }
            .map { location -> location.load(amountLoaded) }
            .flatMap { location -> location.save() }
            .flatMap { location -> location.publishEvents() }
}
