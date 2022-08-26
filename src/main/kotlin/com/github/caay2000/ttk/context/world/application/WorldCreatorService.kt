package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.world.domain.UnknownWorldException
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.WorldConfiguration
import com.github.caay2000.ttk.context.world.domain.WorldException

class WorldCreatorService(
    worldRepository: WorldRepository,
    eventPublisher: EventPublisher<Event>
) : WorldService(worldRepository, eventPublisher) {

    fun invoke(): Either<WorldException, World> =
        createWorld()
            .flatMap { world -> world.save() }
            .flatMap { world -> world.publishEvents() }

    private fun createWorld(): Either<WorldException, World> =
        Either.catch { World.create(WorldConfiguration.get().worldWidth, WorldConfiguration.get().worldHeight) }
            .mapLeft { UnknownWorldException(it) }
}
