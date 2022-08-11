package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World

abstract class LocationService(protected val provider: Provider, protected val eventPublisher: EventPublisher<Event>) {

    protected fun findWorld(): Either<LocationException, World> =
        provider.get()
            .mapLeft { UnknownLocationException(it) }

    protected fun World.findLocation(position: Position): Either<LocationException, Location> =
        Either.catch { getCell(position) }
            .map { cell -> getLocation(cell.locationId!!) }
            .mapLeft { LocationNotFoundByPositionException(position) }

    protected fun Location.save(): Either<LocationException, Location> =
        provider.get()
            .map { world -> world.putLocation(this) }
            .flatMap { world -> provider.set(world) }
            .map { this }
            .mapLeft { UnknownLocationException(it) }

    protected fun Location.publishEvents(): Either<LocationException, Location> =
        Either.catch { eventPublisher.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownLocationException(it) }
}
