package com.github.caay2000.ttk.api.event

interface QueryHandler<in QUERY, out RESPONSE> {

    fun handle(query: QUERY): RESPONSE
}
