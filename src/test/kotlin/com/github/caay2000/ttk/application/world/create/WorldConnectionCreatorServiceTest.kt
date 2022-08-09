package com.github.caay2000.ttk.application.world.create

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.application.pathfinding.PathfindingConfiguration
import com.github.caay2000.ttk.domain.world.Cell
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.infra.eventbus.event.Event
import com.github.caay2000.ttk.infra.eventbus.event.EventPublisher
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mock.EventPublisherMock
import com.github.caay2000.ttk.mother.WorldMother
import com.github.caay2000.ttk.mother.entity.pathfinding.PathfindingConfigurationMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class WorldConnectionCreatorServiceTest {

    private val provider: Provider = DefaultProvider()
    private val eventPublisher: EventPublisher<Event> = EventPublisherMock()
    private val pathfindingConfiguration: PathfindingConfiguration = PathfindingConfigurationMother.default(needConnection = false)

    private val sut = WorldConnectionCreatorService(provider = provider, eventPublisher = eventPublisher, pathfindingConfiguration = pathfindingConfiguration)

    @Test
    fun `should create connection when it does not exists`() {

        `world exists`()

        sut.invoke(Position(0, 0), Position(3, 2)).shouldBeRight {

            Assertions.assertThat(it.connectedCells).isEqualTo(expectedPath)
            Assertions.assertThat(it).isEqualTo(provider.get().bind())
        }
    }

    private fun `world exists`() = provider.set(WorldMother.empty())

    private val expectedPath = setOf(
        Cell(position = Position(x = 0, y = 0), cost = 1),
        Cell(position = Position(x = 1, y = 0), cost = 1),
        Cell(position = Position(x = 2, y = 0), cost = 1),
        Cell(position = Position(x = 2, y = 1), cost = 1),
        Cell(position = Position(x = 3, y = 1), cost = 1),
        Cell(position = Position(x = 3, y = 2), cost = 1)
    )
}
