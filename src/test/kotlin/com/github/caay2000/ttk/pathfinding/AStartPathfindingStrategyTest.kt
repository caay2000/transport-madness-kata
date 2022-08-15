package com.github.caay2000.ttk.pathfinding

import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.mother.WorldMother
import com.github.caay2000.ttk.mother.entity.pathfinding.PathfindingConfigurationMother.default
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AStartPathfindingStrategyTest {

    private val sut: PathfindingStrategy = AStartPathfindingStrategy(default(needConnection = false))

    @Test
    fun `from 0,0 to 0,0 correctly`() {

        sut.invoke(map, cell(0, 0), cell(0, 0)).shouldBeRight {
            assertThat(it.path)
                .hasSize(1)
                .isEqualTo(listOf(cell(0, 0)))
        }
    }

    @Test
    fun `from 0,0 to 0,1 correctly`() {

        sut.invoke(map, cell(0, 0), cell(0, 1)).shouldBeRight {
            assertThat(it.path)
                .hasSize(2)
                .isEqualTo(listOf(cell(0, 0), cell(0, 1)))
        }
    }

    @Test
    fun `from 0,0 to 0,2 correctly`() {

        sut.invoke(map, cell(0, 0), cell(0, 2)).shouldBeRight {
            assertThat(it.path)
                .hasSize(3)
                .isEqualTo(listOf(cell(0, 0), cell(0, 1), cell(0, 2)))
        }
    }

    @Test
    fun `from 0,0 to 3,2 correctly`() {

        sut.invoke(map, cell(0, 0), cell(3, 2)).shouldBeRight {
            assertThat(it.path)
                .hasSize(5)
                .isEqualTo(
                    listOf(
                        Cell(x = 0, y = 0),
                        Cell(x = 1, y = 0),
                        Cell(x = 2, y = 0),
                        Cell(x = 2, y = 1),
                        Cell(x = 3, y = 2)
                    )
                )
        }
    }

    private val map = WorldMother.empty(width = 4, height = 4).cells
    private fun cell(x: Int, y: Int): Cell = map.first { cell -> cell.position == Position(x, y) }
}
