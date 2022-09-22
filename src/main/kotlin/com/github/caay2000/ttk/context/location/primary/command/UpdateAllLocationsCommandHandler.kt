package com.github.caay2000.ttk.context.location.primary.command

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.CommandHandler
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.location.application.AllLocationsUpdaterService
import com.github.caay2000.ttk.context.location.application.LocationRepository
import java.util.UUID

class UpdateAllLocationsCommandHandler(
    locationRepository: LocationRepository,
    eventPublisher: EventPublisher
) : CommandHandler<UpdateAllLocationsCommand> {

    private val allLocationsUpdaterService = AllLocationsUpdaterService(locationRepository, eventPublisher)

    override fun invoke(command: UpdateAllLocationsCommand) =
        allLocationsUpdaterService.invoke().bind()
}

data class UpdateAllLocationsCommand(override val commandId: UUID = UUID.randomUUID()) : Command
