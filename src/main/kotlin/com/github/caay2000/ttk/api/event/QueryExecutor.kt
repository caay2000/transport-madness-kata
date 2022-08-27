package com.github.caay2000.ttk.api.event

interface QueryExecutor<in QUERY : Query, out RESPONSE : QueryResponse> {

    fun <QUERY_1 : QUERY, RESPONSE_1 : @UnsafeVariance RESPONSE> execute(query: QUERY_1): RESPONSE_1
}
