package com.github.caay2000.ttk.context.configuration.application

import com.github.caay2000.ttk.context.location.domain.LocationConfiguration
import com.github.caay2000.ttk.context.world.domain.WorldConfiguration
import com.github.caay2000.ttk.mother.ConfigurationMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class ConfigurationSetterServiceTest {

    private val sut = ConfigurationSetterService()

    @Test
    fun `configuration is stored correctly`() {

        val configuration = ConfigurationMother.random()
        sut.invoke(configuration).shouldBeRight {
            Assertions.assertThat(WorldConfiguration.get()).isEqualTo(WorldConfiguration.fromConfiguration(configuration))
            Assertions.assertThat(LocationConfiguration.get()).isEqualTo(LocationConfiguration.fromConfiguration(configuration))
        }
    }
}
