package com.github.caay2000.ttk.context.entity.query

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryHandler
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.application.EntityNextSectionFinder
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position
import java.util.UUID

class EntityNextSectionQueryHandler(provider: Provider) : QueryHandler<EntityNextSectionQuery, EntityNextSectionQuery.Response> {

    private val entityNextSectionFinder = EntityNextSectionFinder(provider)

    override fun handle(query: EntityNextSectionQuery): EntityNextSectionQuery.Response =
        entityNextSectionFinder.invoke(query.source, query.target)
            .map { nextSection -> EntityNextSectionQuery.Response(query.queryId, nextSection) }
            .bind()
}

data class EntityNextSectionQuery(
    val source: Position,
    val target: Position
) : Query {
    override val queryId: UUID = UUID.randomUUID()

    data class Response(override val queryId: UUID, val path: List<Cell>) : Query.Response
}
