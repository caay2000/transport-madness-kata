package com.github.caay2000.ttk.context.configuration.application

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.context.configuration.secondary.InMemoryConfigurationRepository
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import com.github.caay2000.ttk.mother.ConfigurationMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class ConfigurationSetterServiceTest {

    private val configurationRepository: ConfigurationRepository = InMemoryConfigurationRepository(InMemoryDatabase())
    private val sut = ConfigurationSetterService(configurationRepository)

    @Test
    fun `configuration is stored correctly`() {

        val configuration = ConfigurationMother.random()
        sut.invoke(configuration).shouldBeRight {
            Assertions.assertThat(it).isEqualTo(configuration)
            Assertions.assertThat(it).isEqualTo(configurationRepository.get().bind())
        }
    }
}
