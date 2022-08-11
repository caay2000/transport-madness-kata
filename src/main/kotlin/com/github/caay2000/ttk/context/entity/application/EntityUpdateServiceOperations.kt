package com.github.caay2000.ttk.context.entity.application

import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.configuration.query.GetConfigurationQuery
import com.github.caay2000.ttk.context.entity.domain.EntityUpdateOperations
import com.github.caay2000.ttk.context.entity.query.EntityNextSectionQuery
import com.github.caay2000.ttk.context.location.query.LocationPassengerAvailableQuery
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position

class EntityUpdateServiceOperations(private val queryExecutor: QueryExecutor) : EntityUpdateOperations {

    override fun getConfiguration(): Configuration =
        queryExecutor.execute<GetConfigurationQuery, GetConfigurationQuery.Response>(GetConfigurationQuery()).configuration

    override fun entityNextSection(currentPosition: Position, currentDestination: Position): List<Cell> =
        queryExecutor.execute<EntityNextSectionQuery, EntityNextSectionQuery.Response>(EntityNextSectionQuery(currentPosition, currentDestination)).path

    override fun locationPassengerAvailable(currentPosition: Position): Int =
        queryExecutor.execute<LocationPassengerAvailableQuery, LocationPassengerAvailableQuery.Response>(LocationPassengerAvailableQuery(currentPosition)).available
}
