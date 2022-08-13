package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.world.domain.World

abstract class WorldService(protected val provider: Provider, protected val eventBus: EventPublisher<Event>) {

    protected fun findWorld(): Either<WorldException, World> =
        provider.get()
            .mapLeft { UnknownWorldException(it) }

    protected fun findConfiguration(): Either<WorldException, Configuration> =
        provider.getConfiguration()
            .mapLeft { UnknownWorldException(it) }

    protected fun World.save(): Either<WorldException, World> =
        provider.set(this)
            .mapLeft { UnknownWorldException(it) }

    protected fun World.publishEvents(): Either<WorldException, World> =
        Either.catch { eventBus.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownWorldException(it) }
}