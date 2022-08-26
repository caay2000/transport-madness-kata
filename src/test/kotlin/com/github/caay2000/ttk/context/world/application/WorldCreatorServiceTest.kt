package com.github.caay2000.ttk.context.world.application

import arrow.core.right
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.extension.thenReturnFirstArgument
import com.github.caay2000.ttk.mother.ConfigurationMother
import com.github.caay2000.ttk.mother.WorldMother
import com.github.caay2000.ttk.mother.set
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class WorldCreatorServiceTest {

    private val worldRepository: WorldRepository = mock()
    private val sut = WorldCreatorService(worldRepository, mock())

    private val configuration: Configuration = ConfigurationMother.random().set()

    @Test
    fun `world is created correctly`() {

        `world will be saved`()

        sut.invoke().shouldBeRight {
            assertThat(it).isEqualTo(WorldMother.empty(id = it.id, width = configuration.worldWidth, height = configuration.worldHeight))
            verify(worldRepository).save(it)
        }
    }

    private fun `world will be saved`() {
        whenever(worldRepository.save(any())).thenReturnFirstArgument<World> { it.right() }
    }
}
