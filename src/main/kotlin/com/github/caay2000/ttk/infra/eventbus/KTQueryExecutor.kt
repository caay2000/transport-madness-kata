package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.api.event.QueryResponse

class KTQueryExecutor<in QUERY : Query, out RESPONSE : QueryResponse> : QueryExecutor<QUERY, RESPONSE> {

    override fun <QUERY_1 : QUERY, RESPONSE_1 : @UnsafeVariance RESPONSE> execute(query: QUERY_1): RESPONSE_1 =
        KTEventBus.getInstance<Command, QUERY, Event>().executeQuery(query)
}
