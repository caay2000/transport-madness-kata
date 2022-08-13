package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.location.domain.Location

class LocationUpdaterService(
    provider: Provider,
    eventPublisher: EventPublisher<Event>
) : LocationService(provider, eventPublisher) {

    fun invoke(): Either<LocationException, Unit> =
        findAllLocations()
            .tap { entities -> entities.forEach { it.updateLocation() } }
            .void()

    private fun findAllLocations(): Either<LocationException, Collection<Location>> =
        findWorld()
            .map { world -> world.locations.values }

    private fun Location.updateLocation(): Either<LocationException, Location> =
        this.update().right()
            .flatMap { location -> location.save() }
}
