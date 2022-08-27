package com.github.caay2000.ttk.api.event

interface QueryHandler<in QUERY : Query, out RESPONSE : Any> {

    fun handle(query: QUERY): RESPONSE
}
