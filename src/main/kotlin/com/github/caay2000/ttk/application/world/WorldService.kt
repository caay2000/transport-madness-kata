package com.github.caay2000.ttk.application.world

import arrow.core.Either
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.infra.eventbus.event.Event
import com.github.caay2000.ttk.infra.eventbus.event.EventPublisher

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
