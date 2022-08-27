package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.CommandBus
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.application.EntityRepository
import com.github.caay2000.ttk.context.entity.application.update.EntityUpdaterService
import com.github.caay2000.ttk.context.location.application.LocationRepository
import com.github.caay2000.ttk.context.location.primary.command.UpdateAllLocationsCommand
import com.github.caay2000.ttk.context.world.domain.EntitiesUpdateWorldException
import com.github.caay2000.ttk.context.world.domain.UnknownWorldException
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.WorldException
import com.github.caay2000.ttk.context.world.domain.WorldNotFoundWorldException

class WorldUpdaterService(
    private val worldRepository: WorldRepository,
    locationRepository: LocationRepository,
    entityRepository: EntityRepository,
    private val commandBus: CommandBus<Command>,
    private val eventPublisher: EventPublisher<Event>
) {

    private val entityUpdaterService = EntityUpdaterService(worldRepository, locationRepository, entityRepository, eventPublisher)

    fun invoke(): Either<WorldException, World> =
        Unit.right()
            .flatMap { updateAllLocations() }
            .flatMap { updateAllEntities() }
            .flatMap { updateWorld() }
            .flatMap { world -> world.save() }
            .flatMap { world -> world.publishEvents() }

    private fun findWorld(): Either<WorldException, World> =
        worldRepository.get()
            .mapLeft { WorldNotFoundWorldException(it) }

    private fun updateAllLocations(): Either<WorldException, Unit> =
        commandBus.publish(UpdateAllLocationsCommand()).right()

    private fun updateAllEntities(): Either<WorldException, Unit> =
        entityUpdaterService.invoke()
            .mapLeft { EntitiesUpdateWorldException(it) }

    private fun updateWorld(): Either<WorldException, World> =
        findWorld()
            .map { world -> world.update() }

    private fun World.save(): Either<WorldException, World> =
        worldRepository.save(this)
            .mapLeft { UnknownWorldException(it) }

    private fun World.publishEvents(): Either<WorldException, World> =
        Either.catch { eventPublisher.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownWorldException(it) }
}
