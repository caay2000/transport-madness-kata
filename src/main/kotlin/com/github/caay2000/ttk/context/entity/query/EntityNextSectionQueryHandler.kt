package com.github.caay2000.ttk.context.entity.query

import com.github.caay2000.ttk.api.event.QueryHandler
import com.github.caay2000.ttk.api.provider.Provider

class EntityNextSectionQueryHandler(provider: Provider) : QueryHandler<EntityNextSectionQuery, EntityNextSectionQuery.Response> {

    private val nextSectionFinder = NextSectionFinder(provider)

    override fun handle(query: EntityNextSectionQuery): EntityNextSectionQuery.Response =
        EntityNextSectionQuery.Response(query.queryId, nextSectionFinder.invoke(query.source, query.target))
}
