package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.QueryExecutor

class KTQueryExecutor : QueryExecutor {

    override fun <QUERY, RESPONSE> execute(query: QUERY): RESPONSE = KTEventBus.getInstance<QUERY, Any>().publishQuery(query)
}
