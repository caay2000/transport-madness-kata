package com.github.caay2000.ttk.context.world.application

import arrow.core.right
import com.github.caay2000.ttk.context.configuration.application.ConfigurationRepository
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.extension.thenReturnFirstArgument
import com.github.caay2000.ttk.mother.ConfigurationMother
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class WorldCreatorServiceTest {

    private val worldRepository: WorldRepository = mock()
    private val configurationRepository: ConfigurationRepository = mock()
    private val sut = WorldCreatorService(worldRepository, configurationRepository, mock())

    private val configuration: Configuration = ConfigurationMother.random()

    @Test
    fun `world is created correctly`() {

        `configuration exists`()
        `world will be saved`()

        sut.invoke().shouldBeRight {
            assertThat(it).isEqualTo(WorldMother.empty(id = it.id, width = configuration.worldWidth, height = configuration.worldHeight))
            verify(worldRepository).save(it)
        }
    }

    private fun `configuration exists`() {
        whenever(configurationRepository.get()).thenReturn(configuration.right())
    }

    private fun `world will be saved`() {
        whenever(worldRepository.save(any())).thenReturnFirstArgument<World> { it.right() }
    }
}
