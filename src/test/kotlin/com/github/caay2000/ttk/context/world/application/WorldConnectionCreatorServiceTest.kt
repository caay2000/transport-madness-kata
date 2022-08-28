package com.github.caay2000.ttk.context.world.application

import arrow.core.right
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.extension.thenReturnFirstArgument
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class WorldConnectionCreatorServiceTest {

    private val worldRepository: WorldRepository = mock()

    private val sut = WorldConnectionCreatorService(
        worldRepository = worldRepository,
        eventPublisher = mock()
    )

    @Test
    fun `should create connection when it does not exists`() {

        `world exists`()
        `world will be saved`()

        sut.invoke(Position(0, 0), Position(3, 2)).shouldBeRight {
            assertThat(it.cells.values.filter { cell -> cell.connected }.toSet()).isEqualTo(expectedPath)
            verify(worldRepository).save(it)
        }
    }

    private fun `world exists`() = whenever(worldRepository.get()).thenReturn(WorldMother.empty().right())

    private fun `world will be saved`() {
        whenever(worldRepository.save(any())).thenReturnFirstArgument<World> { it.right() }
    }

    private val expectedPath = setOf(
        Cell(position = Position(x = 0, y = 0), connection = Cell.CellConnection.CONNECTED),
        Cell(position = Position(x = 1, y = 0), connection = Cell.CellConnection.CONNECTED),
        Cell(position = Position(x = 2, y = 0), connection = Cell.CellConnection.CONNECTED),
        Cell(position = Position(x = 2, y = 1), connection = Cell.CellConnection.CONNECTED),
        Cell(position = Position(x = 3, y = 2), connection = Cell.CellConnection.CONNECTED)
    )
}
