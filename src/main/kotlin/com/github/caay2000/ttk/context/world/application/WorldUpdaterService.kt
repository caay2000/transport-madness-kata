package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.application.EntityException
import com.github.caay2000.ttk.context.entity.application.update.EntityUpdaterService
import com.github.caay2000.ttk.context.world.domain.World

class WorldUpdaterService(provider: Provider, eventPublisher: EventPublisher<Event>) : WorldService(provider, eventPublisher) {

    private val entityUpdaterService = EntityUpdaterService(provider, eventPublisher)

    fun invoke(): Either<WorldException, World> =
        findWorld()
            .flatMap { world -> world.updateAllLocations() }
            .flatMap { world -> world.updateAllEntities() }
            .map { world -> world.update() }
            .flatMap { world -> world.save() }
            .flatMap { world -> world.publishEvents() }

    private fun World.updateAllLocations(): Either<WorldException, World> =
        Either.catch {
            locations.values.fold(this) { world, location ->
                location.update().let { world.refreshLocation(it) }
            }
        }.flatMap { world -> world.save() }
            .mapLeft { UnknownWorldException(it) }

    private fun World.updateAllEntities(): Either<WorldException, World> =
        entities.keys.fold(Unit.right() as Either<EntityException, Unit>) { _, entityId -> entityUpdaterService.invoke(entityId).void() }
            .flatMap { provider.get() }
            .mapLeft { UnknownWorldException(it) }
}
