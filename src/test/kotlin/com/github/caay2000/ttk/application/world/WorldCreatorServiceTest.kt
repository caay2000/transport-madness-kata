package com.github.caay2000.ttk.application.world

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.infra.provider.DefaultWorldProvider
import com.github.caay2000.ttk.mother.ConfigurationMother
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class WorldCreatorServiceTest {

    private val provider = DefaultWorldProvider()
    private val sut = WorldCreatorService(provider)

    private val configuration: Configuration = ConfigurationMother.random()

    @Test
    fun `world is created correctly`() {

        println(configuration)
        sut.invoke(configuration).shouldBeRight {
            assertThat(it).isEqualTo(WorldMother.empty(configuration.worldWidth, configuration.worldHeight))
            assertThat(it).isEqualTo(provider.get().bind())
        }
    }
}