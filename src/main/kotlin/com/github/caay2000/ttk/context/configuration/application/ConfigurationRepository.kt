package com.github.caay2000.ttk.context.configuration.application

import arrow.core.Either
import com.github.caay2000.ttk.context.configuration.domain.Configuration

interface ConfigurationRepository {

    fun get(): Either<Throwable, Configuration>
    fun save(configuration: Configuration): Either<Throwable, Configuration>
}
