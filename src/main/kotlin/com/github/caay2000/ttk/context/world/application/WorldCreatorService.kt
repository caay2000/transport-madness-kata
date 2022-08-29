package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.world.domain.UnknownWorldException
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.WorldConfiguration
import com.github.caay2000.ttk.context.world.domain.WorldException

class WorldCreatorService(
    private val worldRepository: WorldRepository,
    private val eventPublisher: EventPublisher
) {

    private val worldService: WorldServiceApi = worldService(worldRepository, eventPublisher)

    fun invoke(): Either<WorldException, World> =
        createWorld()
            .flatMap { world -> worldService.save(world) }
            .flatMap { world -> worldService.publishEvents(world) }

    private fun createWorld(): Either<WorldException, World> =
        Either.catch { World.create(WorldConfiguration.get().worldWidth, WorldConfiguration.get().worldHeight) }
            .mapLeft { UnknownWorldException(it) }
}
