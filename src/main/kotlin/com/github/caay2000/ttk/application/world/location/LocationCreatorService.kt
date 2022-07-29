package com.github.caay2000.ttk.application.world.location

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.application.world.LocationsTooCloseException
import com.github.caay2000.ttk.application.world.WorldService
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.world.Location
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World

class LocationCreatorService(provider: Provider) : WorldService(provider) {

    fun invoke(position: Position, population: Int): Either<Throwable, World> =
        findWorld()
            .flatMap { world -> world.guardMinimumDistance(position) }
            .flatMap { world -> world.createLocation(position, population) }
            .flatMap { world -> world.save() }

    private fun World.guardMinimumDistance(position: Position) =
        findConfiguration()
            .flatMap { configuration ->
                if (anyLocationTooClose(position, configuration)) tooCloseException(position)
                else this.right()
            }

    private fun World.createLocation(position: Position, population: Int): Either<Throwable, World> =
        Either.catch { Location.create(position, population) }
            .map { location -> this.putLocation(location) }

    private fun World.anyLocationTooClose(
        position: Position,
        configuration: Configuration
    ) = this.locations.values.any { it.position.distanceTo(position) < configuration.minDistanceBetweenCities }

    private fun tooCloseException(position: Position) = LocationsTooCloseException("Location $position is too close to another location").left()
}
