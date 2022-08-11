package com.github.caay2000.ttk.mock

import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.context.entity.query.EntityNextSectionQuery
import com.github.caay2000.ttk.context.location.query.LocationPassengerAvailableQuery
import com.github.caay2000.ttk.context.world.domain.Cell
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.util.UUID

class QueryExecutorMocks(private val queryExecutor: QueryExecutor) {

    fun mockLocationPassengerAvailableQuery(amount: Int = 0) {
        whenever(queryExecutor.execute<LocationPassengerAvailableQuery, LocationPassengerAvailableQuery.Response>(any())).thenReturn(
            LocationPassengerAvailableQuery.Response(UUID.randomUUID(), amount)
        )
    }

    fun mockEntityNextSectionQuery(path: List<Cell> = emptyList()) {
        whenever(queryExecutor.execute<EntityNextSectionQuery, EntityNextSectionQuery.Response>(any())).thenReturn(EntityNextSectionQuery.Response(UUID.randomUUID(), path))
    }

}

