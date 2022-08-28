package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.api.event.QueryResponse

class KTQueryExecutor : QueryExecutor {

    override fun <RESPONSE : QueryResponse> execute(query: Query): RESPONSE =
        KTEventBus.getInstance<Command, Query, Event>().executeQuery(query)
}
