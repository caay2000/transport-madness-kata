package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.QueryExecutor

class KTQueryExecutor<in Q, out R> : QueryExecutor<Q, R> {

    override fun <QUERY : Q, RESPONSE : @UnsafeVariance R> execute(query: QUERY): RESPONSE = KTEventBus.getInstance<QUERY, Any>().publishQuery(query)
}
