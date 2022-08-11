package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.shared.LocationId

class LocationFinder(private val provider: Provider) {

    fun invoke(criteria: LocationFinderCriteria): Either<LocationException, Location> =
        findWorld()
            .flatMap { world -> world.findLocation(criteria) }

    private fun World.findLocation(criteria: LocationFinderCriteria): Either<LocationException, Location> =
        when (criteria) {
            is LocationFinderCriteria.ByPosition -> this.findByPosition(criteria.position)
            is LocationFinderCriteria.ByLocationId -> this.findByLocationId(criteria.locationId)
        }

    private fun findWorld(): Either<LocationException, World> =
        provider.get()
            .mapLeft { UnknownLocationException(it) }

    private fun World.findByPosition(position: Position): Either<LocationException, Location> =
        Either.catch { getLocation(position) }
            .mapLeft { LocationNotFoundByPositionException(position) }

    private fun World.findByLocationId(locationId: LocationId): Either<LocationException, Location> =
        Either.catch { getLocation(locationId) }
            .mapLeft { LocationNotFoundByLocationIdException(locationId) }

    sealed class LocationFinderCriteria {

        data class ByPosition(val position: Position) : LocationFinderCriteria()
        data class ByLocationId(val locationId: LocationId) : LocationFinderCriteria()
    }
}
