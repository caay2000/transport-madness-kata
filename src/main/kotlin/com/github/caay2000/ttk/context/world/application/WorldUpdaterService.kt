package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.application.update.EntityUpdaterService
import com.github.caay2000.ttk.context.location.application.LocationUpdaterService
import com.github.caay2000.ttk.context.world.domain.World

class WorldUpdaterService(provider: Provider, eventPublisher: EventPublisher<Event>) : WorldService(provider, eventPublisher) {

    private val entityUpdaterService = EntityUpdaterService(provider, eventPublisher)
    private val locationUpdaterService = LocationUpdaterService(provider, eventPublisher)

    fun invoke(): Either<WorldException, World> =
        Unit.right()
            .tap { updateAllLocations() }
            .tap { updateAllEntities() }
            .flatMap { updateWorld() }
            .flatMap { world -> world.save() }
            .flatMap { world -> world.publishEvents() }

    private fun updateAllLocations(): Either<WorldException, Unit> =
        locationUpdaterService.invoke()
            .mapLeft { UnknownWorldException(it) }

    private fun updateAllEntities(): Either<WorldException, Unit> =
        entityUpdaterService.invoke()
            .mapLeft { EntitiesUpdateWorldException(it) }

    private fun updateWorld(): Either<WorldException, World> =
        findWorld()
            .map { world -> world.update() }
}
