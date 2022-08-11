package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryHandler
import kotlin.reflect.KClass

abstract class KTQueryHandler<in QUERY, out RESPONSE>(type: KClass<*>) : QueryHandler<QUERY, RESPONSE> {

    init {
        subscribeTo(type)
    }

    private fun subscribeTo(type: KClass<*>) {
        KTEventBus.getInstance<QUERY, Any>().subscribe(this, type)
    }

    internal fun execute(query: Any): RESPONSE {
        @Suppress("UNCHECKED_CAST")
        return this.handle(query as QUERY)
    }

    abstract override fun handle(query: QUERY): RESPONSE
}

internal fun <QUERY : Query, RESPONSE : Query.Response> instantiateQueryHandler(
    clazz: KClass<QUERY>,
    queryHandler: QueryHandler<QUERY, RESPONSE>
): KTQueryHandler<QUERY, RESPONSE> =
    object : KTQueryHandler<QUERY, RESPONSE>(clazz) {
        override fun handle(query: QUERY): RESPONSE = queryHandler.handle(query)
    }
