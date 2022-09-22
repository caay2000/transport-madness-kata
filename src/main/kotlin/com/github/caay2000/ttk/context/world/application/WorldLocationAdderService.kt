package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.WorldException
import com.github.caay2000.ttk.shared.LocationId

class WorldLocationAdderService(
    private val worldRepository: WorldRepository,
    private val eventPublisher: EventPublisher
) {

    private val worldService: WorldServiceApi = worldService(worldRepository, eventPublisher)

    fun invoke(locationId: LocationId, position: Position): Either<WorldException, World> =
        worldService.find()
            .map { world -> world.addLocation(locationId, position) }
            .flatMap { world -> worldService.save(world) }
            .flatMap { world -> worldService.publishEvents(world) }
}
