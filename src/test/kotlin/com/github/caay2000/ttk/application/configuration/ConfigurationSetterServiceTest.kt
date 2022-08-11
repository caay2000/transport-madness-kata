package com.github.caay2000.ttk.application.configuration

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.context.configuration.application.ConfigurationSetterService
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mother.ConfigurationMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class ConfigurationSetterServiceTest {

    private val provider = DefaultProvider()
    private val sut = ConfigurationSetterService(provider)

    @Test
    fun `configuration is stored correctly`() {

        val configuration = ConfigurationMother.random()
        sut.invoke(configuration).shouldBeRight {
            Assertions.assertThat(it).isEqualTo(configuration)
            Assertions.assertThat(it).isEqualTo(provider.getConfiguration().bind())
        }
    }
}
