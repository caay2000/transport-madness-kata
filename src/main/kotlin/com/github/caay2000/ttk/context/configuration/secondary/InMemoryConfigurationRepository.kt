package com.github.caay2000.ttk.context.configuration.secondary

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.configuration.application.ConfigurationRepository
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.infra.database.InMemoryDatabase

class InMemoryConfigurationRepository(private val db: InMemoryDatabase) : ConfigurationRepository {

    override fun get(): Either<Throwable, Configuration> =
        Either.catch { db.getById<Configuration>(TABLE_NAME, CONFIG_ID) }
            .flatMap { it?.right() ?: NoSuchElementException().left() }

    override fun save(configuration: Configuration): Either<Throwable, Configuration> =
        Either.catch {
            db.save(TABLE_NAME, CONFIG_ID, configuration)
        }.map { configuration }

    companion object {
        const val TABLE_NAME = "configuration"
        const val CONFIG_ID = "1"
    }
}
