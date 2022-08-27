package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryHandler
import com.github.caay2000.ttk.api.event.QueryResponse
import kotlin.reflect.KClass

abstract class KTQueryHandler<in QUERY : Query, out RESPONSE : QueryResponse>(type: KClass<QUERY>) : QueryHandler<QUERY, RESPONSE> {

    init {
        subscribeTo(type)
    }

    private fun subscribeTo(type: KClass<QUERY>) {
        KTEventBus.getInstance<Command, QUERY, Event>().subscribe(this, type)
    }

    internal fun <RESPONSE_1 : @UnsafeVariance RESPONSE> execute(query: QUERY): RESPONSE_1 =
        @Suppress("UNCHECKED_CAST")
        (this.handle(query) as RESPONSE_1).updateQueryId(query)

    private fun <RESPONSE_1 : RESPONSE> RESPONSE_1.updateQueryId(query: QUERY): RESPONSE_1 =
        this.also {
            it::class.java.getDeclaredField("queryId").let { field ->
                field.isAccessible = true
                field.set(this, query.queryId)
                field.isAccessible = false
            }
        }

    abstract override fun handle(query: QUERY): RESPONSE
}

internal fun <QUERY : Query, RESPONSE : QueryResponse> instantiateQueryHandler(clazz: KClass<QUERY>, queryHandler: QueryHandler<QUERY, RESPONSE>): KTQueryHandler<QUERY, RESPONSE> =
    object : KTQueryHandler<QUERY, RESPONSE>(clazz) {
        override fun handle(query: QUERY): RESPONSE = queryHandler.handle(query)
    }
