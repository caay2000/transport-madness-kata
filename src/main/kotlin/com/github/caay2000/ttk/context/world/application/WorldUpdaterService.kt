package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.api.event.CommandBus
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.primary.command.UpdateAllCompaniesCommand
import com.github.caay2000.ttk.context.location.primary.command.UpdateAllLocationsCommand
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.WorldException

class WorldUpdaterService(
    worldRepository: WorldRepository,
    private val commandBus: CommandBus,
    eventPublisher: EventPublisher
) {

    private val worldService: WorldServiceApi = worldService(worldRepository, eventPublisher)

    fun invoke(): Either<WorldException, World> =
        Unit.right()
            .flatMap { updateAllLocations() }
            .flatMap { updateAllCompanies() }
            .flatMap { updateWorld() }
            .flatMap { world -> worldService.save(world) }
            .flatMap { world -> worldService.publishEvents(world) }

    private fun updateAllLocations(): Either<WorldException, Unit> =
        commandBus.publish(UpdateAllLocationsCommand()).right()

    private fun updateAllCompanies(): Either<WorldException, Unit> =
        commandBus.publish(UpdateAllCompaniesCommand()).right()

    private fun updateWorld(): Either<WorldException, World> =
        worldService.find()
            .map { world -> world.update() }
}
