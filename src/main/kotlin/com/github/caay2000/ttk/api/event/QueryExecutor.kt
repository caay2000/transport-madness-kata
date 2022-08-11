package com.github.caay2000.ttk.api.event

interface QueryExecutor<in Q, out R> {

    fun <QUERY : Q, RESPONSE : @UnsafeVariance R> execute(query: QUERY): RESPONSE
}
