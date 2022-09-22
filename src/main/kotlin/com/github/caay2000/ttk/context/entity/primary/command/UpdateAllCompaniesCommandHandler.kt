package com.github.caay2000.ttk.context.entity.primary.command

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.CommandHandler
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.context.entity.application.EntityAllCompanyVehiclesUpdaterService
import com.github.caay2000.ttk.context.entity.application.EntityRepository
import com.github.caay2000.ttk.shared.CompanyId
import java.util.UUID

class UpdateAllCompanyVehiclesCommandHandler(
    entityRepository: EntityRepository,
    queryExecutor: QueryExecutor,
    eventPublisher: EventPublisher
) : CommandHandler<UpdateAllCompanyVehiclesCommand> {

    private val entityAllCompanyVehiclesUpdaterService = EntityAllCompanyVehiclesUpdaterService(
        entityRepository,
        queryExecutor,
        eventPublisher
    )

    override fun invoke(command: UpdateAllCompanyVehiclesCommand) =
        entityAllCompanyVehiclesUpdaterService.invoke(command.companyId).bind()
}

data class UpdateAllCompanyVehiclesCommand(val companyId: CompanyId) : Command {
    override val commandId: UUID = UUID.randomUUID()
}