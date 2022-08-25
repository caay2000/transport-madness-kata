package com.github.caay2000.ttk.context.world.application

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.configuration.application.ConfigurationRepository
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mother.WorldMother
import com.github.caay2000.ttk.mother.entity.pathfinding.PathfindingConfigurationMother
import com.github.caay2000.ttk.pathfinding.PathfindingConfiguration
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class WorldConnectionCreatorServiceTest {

    private val provider: Provider = DefaultProvider()
    private val configurationRepository: ConfigurationRepository = mock()
    private val pathfindingConfiguration: PathfindingConfiguration = PathfindingConfigurationMother.default(needConnection = false)

    private val sut = WorldConnectionCreatorService(
        provider = provider,
        configurationRepository = configurationRepository,
        eventPublisher = mock(),
        pathfindingConfiguration = pathfindingConfiguration
    )

    @Test
    fun `should create connection when it does not exists`() {

        `world exists`()

        sut.invoke(Position(0, 0), Position(3, 2)).shouldBeRight {

            assertThat(it.connectedCells).isEqualTo(expectedPath)
            assertThat(it).isEqualTo(provider.get().bind())
        }
    }

    private fun `world exists`() = provider.set(WorldMother.empty())

    private val expectedPath = setOf(
        Cell(position = Position(x = 0, y = 0), connection = Cell.CellConnection.CONNECTED),
        Cell(position = Position(x = 1, y = 0), connection = Cell.CellConnection.CONNECTED),
        Cell(position = Position(x = 2, y = 0), connection = Cell.CellConnection.CONNECTED),
        Cell(position = Position(x = 2, y = 1), connection = Cell.CellConnection.CONNECTED),
        Cell(position = Position(x = 3, y = 2), connection = Cell.CellConnection.CONNECTED)
    )
}
