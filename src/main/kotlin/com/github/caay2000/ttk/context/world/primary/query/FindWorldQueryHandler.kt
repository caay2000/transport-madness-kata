package com.github.caay2000.ttk.context.world.primary.query

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryHandler
import com.github.caay2000.ttk.api.event.QueryResponse
import com.github.caay2000.ttk.context.world.application.WorldFinderService
import com.github.caay2000.ttk.context.world.application.WorldRepository
import com.github.caay2000.ttk.context.world.domain.World
import java.util.UUID

class FindWorldQueryHandler(worldRepository: WorldRepository) : QueryHandler<FindWorldQuery, FindWorldQueryResponse> {

    private val worldFinder = WorldFinderService(worldRepository)

    override fun handle(query: FindWorldQuery): FindWorldQueryResponse =
        worldFinder.invoke()
            .map { FindWorldQueryResponse(it) }
            .bind()
}

class FindWorldQuery : Query {
    override val queryId: UUID = UUID.randomUUID()
}

data class FindWorldQueryResponse(override val value: World) : QueryResponse
