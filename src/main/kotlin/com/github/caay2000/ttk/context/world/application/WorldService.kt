package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.configuration.application.ConfigurationRepository
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.world.domain.ConfigurationNotFoundWorldException
import com.github.caay2000.ttk.context.world.domain.UnknownWorldException
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.WorldException
import com.github.caay2000.ttk.context.world.domain.WorldNotFoundWorldException

abstract class WorldService(
    private val provider: Provider,
    private val configurationRepository: ConfigurationRepository,
    private val eventBus: EventPublisher<Event>
) {

    protected fun findWorld(): Either<WorldException, World> =
        provider.get()
            .mapLeft { WorldNotFoundWorldException(it) }

    protected fun findConfiguration(): Either<WorldException, Configuration> =
        configurationRepository.get()
            .mapLeft { ConfigurationNotFoundWorldException(it) }

    protected fun World.save(): Either<WorldException, World> =
        provider.set(this)
            .mapLeft { UnknownWorldException(it) }

    protected fun World.publishEvents(): Either<WorldException, World> =
        Either.catch { eventBus.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownWorldException(it) }
}
