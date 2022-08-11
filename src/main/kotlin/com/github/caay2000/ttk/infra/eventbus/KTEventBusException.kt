package com.github.caay2000.ttk.infra.eventbus

sealed class KTEventBusException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

data class QueryHandlerNotFoundException(val query: Any) : KTEventBusException("Configuration not found $query")
