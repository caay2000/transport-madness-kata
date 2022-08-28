package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.api.event.QueryResponse

class KTQueryExecutor : QueryExecutor {

    override fun <QUERY : Query, RESPONSE : QueryResponse> execute(query: QUERY): RESPONSE =
        KTEventBus.getInstance<Command, QUERY, Event>().executeQuery(query)
}
