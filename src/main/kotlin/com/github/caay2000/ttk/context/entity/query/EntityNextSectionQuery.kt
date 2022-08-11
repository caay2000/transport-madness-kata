package com.github.caay2000.ttk.context.entity.query

import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position
import java.util.UUID

data class EntityNextSectionQuery(
    val source: Position,
    val target: Position
) : Query {
    override val queryId: UUID = UUID.randomUUID()

    data class Response(override val queryId: UUID, val path: List<Cell>) : Query.Response
}
