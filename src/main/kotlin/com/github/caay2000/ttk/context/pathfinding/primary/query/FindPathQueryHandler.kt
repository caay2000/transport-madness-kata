package com.github.caay2000.ttk.context.pathfinding.primary.query

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryHandler
import com.github.caay2000.ttk.api.event.QueryResponse
import com.github.caay2000.ttk.context.pathfinding.application.PathFinder
import com.github.caay2000.ttk.context.pathfinding.domain.PathfindingConfiguration
import com.github.caay2000.ttk.context.world.domain.Cell
import java.util.UUID

class FindPathQueryHandler : QueryHandler<FindPathQuery, FindPathQueryResponse> {

    private val pathFinder = PathFinder()

    override fun handle(query: FindPathQuery): FindPathQueryResponse =
        pathFinder.invoke(pathfindingConfiguration(query), query.cells, query.source, query.target)
            .map { section -> FindPathQueryResponse(section.value) }
            .bind()

    private fun pathfindingConfiguration(query: FindPathQuery): PathfindingConfiguration =
        PathfindingConfiguration(query.needConnection)
}

data class FindPathQuery(val cells: Collection<Cell>, val source: Cell, val target: Cell, val needConnection: Boolean) : Query {
    override val queryId: UUID = UUID.randomUUID()
}

data class FindPathQueryResponse(override val value: List<Cell>) : QueryResponse
