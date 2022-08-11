package com.github.caay2000.ttk.context.location.query

import arrow.core.getOrHandle
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryHandler
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.location.application.LocationFinder
import com.github.caay2000.ttk.context.world.domain.Position
import java.util.UUID

class LocationPassengerAvailableQueryHandler(provider: Provider) : QueryHandler<LocationPassengerAvailableQuery, LocationPassengerAvailableQuery.Response> {

    private val locationFinder = LocationFinder(provider)

    override fun handle(query: LocationPassengerAvailableQuery): LocationPassengerAvailableQuery.Response =
        locationFinder.invoke(LocationFinder.LocationFinderCriteria.ByPosition(query.position))
            .map { LocationPassengerAvailableQuery.Response(query.queryId, it.pax) }
            .getOrHandle { LocationPassengerAvailableQuery.Response(query.queryId, 0) }
    // TODO change this getOrHandle to a simple bind and avoid defaulting to 0
}

class LocationPassengerAvailableQuery(val position: Position) : Query {
    override val queryId: UUID = UUID.randomUUID()

    data class Response(override val queryId: UUID, val available: Int) : Query.Response
}
