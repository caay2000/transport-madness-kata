package com.github.caay2000.ttk.context.company.primary.command

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.CommandBus
import com.github.caay2000.ttk.api.event.CommandHandler
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.application.AllCompaniesUpdaterService
import com.github.caay2000.ttk.context.company.application.CompanyRepository
import java.util.UUID

class UpdateAllCompaniesCommandHandler(
    companyRepository: CompanyRepository,
    commandBus: CommandBus,
    eventPublisher: EventPublisher
) : CommandHandler<UpdateAllCompaniesCommand> {

    private val allCompaniesUpdaterService = AllCompaniesUpdaterService(companyRepository, commandBus, eventPublisher)

    override fun invoke(command: UpdateAllCompaniesCommand) =
        allCompaniesUpdaterService.invoke().bind()
}

data class UpdateAllCompaniesCommand(override val commandId: UUID = UUID.randomUUID()) : Command
