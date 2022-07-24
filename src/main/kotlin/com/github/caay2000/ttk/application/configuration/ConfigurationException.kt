package com.github.caay2000.ttk.application.configuration

sealed class ConfigurationException : RuntimeException {
    constructor(cause: Throwable) : super(cause)
}

data class UnknownConfigurationException(override val cause: Throwable) : ConfigurationException(cause)
