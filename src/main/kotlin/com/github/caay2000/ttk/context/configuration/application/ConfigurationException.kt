package com.github.caay2000.ttk.context.configuration.application

sealed class ConfigurationException(cause: Throwable) : RuntimeException(cause)

data class UnknownConfigurationException(override val cause: Throwable) : ConfigurationException(cause)
