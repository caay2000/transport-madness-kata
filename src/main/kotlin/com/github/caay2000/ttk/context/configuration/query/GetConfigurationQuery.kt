package com.github.caay2000.ttk.context.configuration.query

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryHandler
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.configuration.application.ConfigurationFinder
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import java.util.UUID

class GetConfigurationQueryHandler(provider: Provider) : QueryHandler<GetConfigurationQuery, GetConfigurationQuery.Response> {

    private val configurationFinder = ConfigurationFinder(provider)

    override fun handle(query: GetConfigurationQuery): GetConfigurationQuery.Response =
        configurationFinder.invoke()
            .map { configuration -> GetConfigurationQuery.Response(query.queryId, configuration) }
            .bind()
}

class GetConfigurationQuery : Query {
    override val queryId: UUID = UUID.randomUUID()

    data class Response(override val queryId: UUID, val configuration: Configuration) : Query.Response
}
