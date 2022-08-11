package com.github.caay2000.ttk.api.event

interface QueryExecutor {

    fun <QUERY, RESPONSE> execute(query: QUERY): RESPONSE
}
