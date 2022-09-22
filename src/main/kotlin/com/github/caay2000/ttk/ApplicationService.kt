package com.github.caay2000.ttk

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityType
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.EntityId

class ApplicationService(inMemoryDatabase: InMemoryDatabase) : ApplicationServiceApi {

    private val application: Application = Application(inMemoryDatabase)

    override fun setConfiguration(configuration: Configuration) {
        application.configurationSetterService.invoke(configuration)
    }

    override fun createWorld(): World = application.worldCreatorService.invoke().bind()
    override fun createConnection(companyId: CompanyId, source: Position, destination: Position): World =
        application.worldConnectionCreatorService.invoke(companyId, source, destination).bind()

    override fun createLocation(name: String, position: Position, population: Int): Location =
        application.locationCreatorService.invoke(name, position, population).bind()

    override fun createCompany(name: String): Company = application.companyCreatorService.invoke(name).bind()

    override fun createEntity(companyId: CompanyId, entityType: EntityType, position: Position): Entity =
        application.entityCreatorService.invoke(companyId, entityType, position).bind()

    override fun assignRoute(entityId: EntityId, route: List<Position>): Entity =
        application.entityRouteAssignerService.invoke(entityId, route).bind()

    override fun passTurn(print: Boolean): World = application.worldUpdaterService.invoke().bind()
        .also { if (print) application.printer.print(it) }

    override fun printWorld() = application.printer.print()
}
