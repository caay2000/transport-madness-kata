package com.github.caay2000.ttk.api.event

interface QueryExecutor {

    fun <RESPONSE : @UnsafeVariance QueryResponse> execute(query: Query): RESPONSE
}
