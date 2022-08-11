package com.github.caay2000.ttk.api.event

object DomainQueryExecutor : QueryExecutor<Query, Query.Response> {

    private lateinit var queryExecutor: QueryExecutor<Query, Query.Response>

    fun init(queryExecutor: QueryExecutor<Query, Query.Response>) {
        DomainQueryExecutor.queryExecutor = queryExecutor
    }

    override fun <QUERY : Query, RESPONSE : Query.Response> execute(query: QUERY): RESPONSE = queryExecutor.execute(query)
}
