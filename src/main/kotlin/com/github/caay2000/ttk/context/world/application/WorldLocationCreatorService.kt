package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World

class WorldLocationCreatorService(provider: Provider, eventPublisher: EventPublisher<Event>) : WorldService(provider, eventPublisher) {

    fun invoke(position: Position, population: Int): Either<Throwable, World> =
        findWorld()
            .flatMap { world -> world.guardMinimumDistance(position) }
            .flatMap { world -> world.createLocation(position, population) }
            .flatMap { world -> world.save() }
            .flatMap { world -> world.publishEvents() }

    private fun World.guardMinimumDistance(position: Position) =
        findConfiguration()
            .flatMap { configuration ->
                if (anyLocationTooClose(position, configuration)) tooCloseException(position)
                else this.right()
            }

    private fun World.createLocation(position: Position, population: Int): Either<Throwable, World> =
        findConfiguration()
            .map { configuration -> Location.create(position, population, configuration) }
            .map { location -> putLocation(location) }

    private fun World.anyLocationTooClose(
        position: Position,
        configuration: Configuration
    ) = locations.values.any { it.position.distanceTo(position) < configuration.minDistanceBetweenCities }

    private fun tooCloseException(position: Position) = LocationsTooCloseException("Location $position is too close to another location").left()
}
