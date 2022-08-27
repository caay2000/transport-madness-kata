package com.github.caay2000.ttk

import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityType
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.EntityId

interface ApplicationServiceApi {

    fun setConfiguration(configuration: Configuration)

    fun createWorld(): World
    fun createConnection(source: Position, destination: Position): World

    fun createLocation(name: String, position: Position, population: Int): Location

    fun createCompany(name: String): Company

    fun createEntity(companyId: CompanyId, entityType: EntityType, position: Position): Entity
    fun assignRoute(entityId: EntityId, route: List<Position>): Entity

    fun passTurn(print: Boolean = false): World
    fun printWorld()
}
