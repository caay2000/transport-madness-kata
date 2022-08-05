package com.github.caay2000.ttk.application.world.create

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.application.world.UnknownWorldException
import com.github.caay2000.ttk.application.world.WorldException
import com.github.caay2000.ttk.application.world.WorldService
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.infra.eventbus.event.Event
import com.github.caay2000.ttk.infra.eventbus.event.EventPublisher

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
