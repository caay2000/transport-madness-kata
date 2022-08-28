package com.github.caay2000.ttk.context.location.primary.query

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryHandler
import com.github.caay2000.ttk.api.event.QueryResponse
import com.github.caay2000.ttk.context.location.application.LocationFinder
import com.github.caay2000.ttk.context.location.application.LocationRepository
import com.github.caay2000.ttk.context.location.application.LocationRepository.FindLocationCriteria
import com.github.caay2000.ttk.context.location.domain.Location
import java.util.UUID

class LocationFinderQueryHandler(locationRepository: LocationRepository) : QueryHandler<LocationFinderQuery, LocationFinderQueryResponse> {

    private val locationFinder = LocationFinder(locationRepository)

    override fun handle(query: LocationFinderQuery): LocationFinderQueryResponse =
        locationFinder.invoke(query.criteria)
            .map { LocationFinderQueryResponse(it) }
            .bind()
}

data class LocationFinderQuery(val criteria: FindLocationCriteria) : Query {
    override val queryId: UUID = UUID.randomUUID()
}

data class LocationFinderQueryResponse(override val value: Location) : QueryResponse {
    override val queryId: UUID = UUID.randomUUID()
}
