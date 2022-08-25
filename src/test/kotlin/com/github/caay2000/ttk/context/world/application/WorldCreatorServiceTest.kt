package com.github.caay2000.ttk.context.world.application

import arrow.core.computations.ResultEffect.bind
import arrow.core.right
import com.github.caay2000.ttk.context.configuration.application.ConfigurationRepository
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mother.ConfigurationMother
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class WorldCreatorServiceTest {

    private val provider = DefaultProvider()
    private val configurationRepository: ConfigurationRepository = mock()
    private val sut = WorldCreatorService(provider, configurationRepository, mock())

    private val configuration: Configuration = ConfigurationMother.random()

    @Test
    fun `world is created correctly`() {

        whenever(configurationRepository.get()).thenReturn(configuration.right())

        sut.invoke().shouldBeRight {
            assertThat(it).isEqualTo(WorldMother.empty(id = it.id, width = configuration.worldWidth, height = configuration.worldHeight))
            assertThat(it).isEqualTo(provider.get().bind())
        }
    }
}
