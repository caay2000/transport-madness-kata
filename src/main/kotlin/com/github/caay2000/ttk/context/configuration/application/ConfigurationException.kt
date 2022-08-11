package com.github.caay2000.ttk.context.configuration.application

sealed class ConfigurationException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

object ConfigurationNotFoundException : ConfigurationException("Configuration not found")
data class UnknownConfigurationException(override val cause: Throwable) : ConfigurationException(cause)
