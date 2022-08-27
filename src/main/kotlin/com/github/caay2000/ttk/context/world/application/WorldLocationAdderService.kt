package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.UnknownWorldException
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.WorldException
import com.github.caay2000.ttk.shared.LocationId

class WorldLocationAdderService(
    private val worldRepository: WorldRepository,
    private val eventPublisher: EventPublisher<Event>
) {

    fun invoke(locationId: LocationId, position: Position): Either<WorldException, World> =
        findWorld()
            .map { world -> world.addLocation(locationId, position) }
            .flatMap { world -> world.save() }
            .flatMap { world -> world.publishEvents() }

    private fun findWorld(): Either<WorldException, World> =
        worldRepository.get()
            .mapLeft { UnknownWorldException(it) }

    private fun World.save(): Either<WorldException, World> =
        worldRepository.save(this)
            .map { this }
            .mapLeft { UnknownWorldException(it) }

    private fun World.publishEvents(): Either<WorldException, World> =
        Either.catch { eventPublisher.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownWorldException(it) }
}
