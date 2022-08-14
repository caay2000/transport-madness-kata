package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.world.domain.UnknownWorldException
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.WorldException

class WorldCreatorService(provider: Provider, eventPublisher: EventPublisher<Event>) : WorldService(provider, eventPublisher) {

    fun invoke(): Either<WorldException, World> =
        findConfiguration()
            .flatMap { configuration -> createWorld(configuration) }
            .flatMap { world -> world.save() }
            .flatMap { world -> world.publishEvents() }

    private fun createWorld(configuration: Configuration): Either<WorldException, World> =
        Either.catch { World.create(configuration.worldWidth, configuration.worldHeight) }
            .mapLeft { UnknownWorldException(it) }
}
