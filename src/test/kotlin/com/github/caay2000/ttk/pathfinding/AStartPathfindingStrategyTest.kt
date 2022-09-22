package com.github.caay2000.ttk.pathfinding

import com.github.caay2000.ttk.context.pathfinding.domain.AStartPathfindingStrategy
import com.github.caay2000.ttk.context.pathfinding.domain.PathfindingStrategy
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.mother.WorldMother
import com.github.caay2000.ttk.mother.entity.pathfinding.PathfindingConfigurationMother.default
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AStartPathfindingStrategyTest {

    private val sut: PathfindingStrategy = AStartPathfindingStrategy()

    @Test
    fun `from 0,0 to 0,0 correctly`() {

        sut.invoke(default(needConnection = false), map.cells.values, map.getCell(0, 0), map.getCell(0, 0)).shouldBeRight {
            assertThat(it.path)
                .hasSize(1)
                .isEqualTo(listOf(map.getCell(0, 0)))
        }
    }

    @Test
    fun `from 0,0 to 0,1 correctly`() {

        sut.invoke(default(needConnection = false), map.cells.values, map.getCell(0, 0), map.getCell(0, 1)).shouldBeRight {
            assertThat(it.path)
                .hasSize(2)
                .isEqualTo(listOf(map.getCell(0, 0), map.getCell(0, 1)))
        }
    }

    @Test
    fun `from 0,0 to 0,2 correctly`() {

        sut.invoke(default(needConnection = false), map.cells.values, map.getCell(0, 0), map.getCell(0, 2)).shouldBeRight {
            assertThat(it.path)
                .hasSize(3)
                .isEqualTo(listOf(map.getCell(0, 0), map.getCell(0, 1), map.getCell(0, 2)))
        }
    }

    @Test
    fun `from 0,0 to 3,2 correctly`() {

        sut.invoke(default(needConnection = false), map.cells.values, map.getCell(0, 0), map.getCell(3, 2)).shouldBeRight {
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

    @Test
    fun `from 0,0 to 0,0 correctly when connection is needed`() {

        sut.invoke(default(needConnection = true), connectedMap.cells.values, connectedMap.getCell(0, 0), connectedMap.getCell(0, 0)).shouldBeRight {
            assertThat(it.path)
                .hasSize(1)
                .isEqualTo(listOf(connectedMap.getCell(0, 0)))
        }
    }

    @Test
    fun `from 0,0 to 0,1 correctly when connection is needed`() {

        sut.invoke(default(needConnection = true), connectedMap.cells.values, connectedMap.getCell(0, 0), connectedMap.getCell(0, 1)).shouldBeRight {
            assertThat(it.path)
                .hasSize(2)
                .isEqualTo(listOf(connectedMap.getCell(0, 0), connectedMap.getCell(0, 1)))
        }
    }

    @Test
    fun `from 0,0 to 0,2 correctly when connection is needed`() {

        sut.invoke(default(needConnection = true), connectedMap.cells.values, connectedMap.getCell(0, 0), connectedMap.getCell(0, 2)).shouldBeRight {
            assertThat(it.path)
                .hasSize(3)
                .isEqualTo(listOf(connectedMap.getCell(0, 0), connectedMap.getCell(0, 1), connectedMap.getCell(0, 2)))
        }
    }

    @Test
    fun `from 0,0 to 3,2 correctly when connection is needed`() {

        sut.invoke(default(needConnection = true), connectedMap.cells.values, connectedMap.getCell(0, 0), connectedMap.getCell(3, 2)).shouldBeRight {
            assertThat(it.path).hasSize(5)
        }
    }

    private val map = WorldMother.empty(
        width = 4,
        height = 4
    )
    private val connectedMap = WorldMother.random(
        width = 4,
        height = 4,
        connectedPaths = mapOf(Position(0, 0) to listOf(Position(0, 2), Position(3, 2)))
    )

    private fun World.getCell(x: Int, y: Int) = this.getCell(Position(x, y))
}
