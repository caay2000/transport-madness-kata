package com.github.caay2000.ttk.context.pathfinding.primary.query

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryHandler
import com.github.caay2000.ttk.api.event.QueryResponse
import com.github.caay2000.ttk.context.pathfinding.application.NextSectionFinder
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.pathfinding.PathfindingConfiguration
import java.util.UUID

class FindNextSectionQueryHandler(pathfindingConfiguration: PathfindingConfiguration) : QueryHandler<FindNextSectionQuery, FindNextSectionQueryResponse> {

    private val nextSectionFinder = NextSectionFinder(pathfindingConfiguration)

    override fun handle(query: FindNextSectionQuery): FindNextSectionQueryResponse =
        nextSectionFinder.invoke(query.cells, query.source, query.target)
            .map { section -> FindNextSectionQueryResponse(section.value) }
            .bind()
}

data class FindNextSectionQuery(val cells: List<Cell>, val source: Cell, val target: Cell) : Query {
    override val queryId: UUID = UUID.randomUUID()
}

data class FindNextSectionQueryResponse(override val value: List<Cell>) : QueryResponse
