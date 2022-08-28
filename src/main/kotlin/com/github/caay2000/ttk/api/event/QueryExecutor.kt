package com.github.caay2000.ttk.api.event

interface QueryExecutor {

    fun <QUERY : Query, RESPONSE : @UnsafeVariance QueryResponse> execute(query: QUERY): RESPONSE
}
