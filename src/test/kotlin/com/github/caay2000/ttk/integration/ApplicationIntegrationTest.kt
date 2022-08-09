package com.github.caay2000.ttk.integration

import com.github.caay2000.ttk.application.Application
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mother.ConfigurationMother
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ApplicationIntegrationTest {

    private val configuration = ConfigurationMother.random(worldWidth = 4, worldHeight = 6, minDistanceBetweenCities = 1)
    private val provider: Provider = DefaultProvider()

    @ParameterizedTest
    @MethodSource("exercise 3 data")
    fun `execise 3`(startPosition: Position, paths: Map<Position, List<Position>>, route: List<Position>, turns: Int) {

        val sut = Application(configuration, provider)

        assertThat(
            sut.invoke(
                startPosition = startPosition,
                paths = paths,
                locations = setOf((Position(0, 0) to 500), (Position(3, 2) to 1000), (Position(1, 4) to 250)),
                route = route
            )
        ).isEqualTo(turns)
    }

    companion object {
        @JvmStatic
        fun `exercise 3 data`(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(Position(0, 0), `path from 0,0 to 3,2`(), `route from 0,0 to 3,2`(), 12),
                Arguments.of(Position(0, 0), `path from 0,0 to 1,4`(), `route from 0,0 to 1,4`(), 12),
                Arguments.of(Position(0, 0), `path from 0,0 to 3,2 to 1,4`(), `route from 0,0 to 3,2 to 1,4`(), 21),
                Arguments.of(Position(0, 0), `path from 0,0 to 3,2 to 1,4 to 0,0`(), `route from 0,0 to 3,2 to 1,4`(), 17),
                Arguments.of(Position(0, 0), `path from 0,0 to 3,2 to 1,4 to 0,0`(), `route from 0,0 to 3,2 to 1,4 to 3,2`(), 22)
            )
        }

        private fun `route from 0,0 to 3,2`() = listOf(Position(0, 0), Position(3, 2))
        private fun `route from 0,0 to 1,4`() = listOf(Position(0, 0), Position(1, 4))
        private fun `route from 0,0 to 3,2 to 1,4`() = listOf(Position(0, 0), Position(3, 2), Position(1, 4))
        private fun `route from 0,0 to 3,2 to 1,4 to 3,2`() = listOf(Position(0, 0), Position(3, 2), Position(1, 4), Position(3, 2))

        private fun `path from 0,0 to 3,2`() = mapOf(Position(0, 0) to listOf(Position(3, 2)))
        private fun `path from 0,0 to 1,4`() = mapOf(Position(0, 0) to listOf(Position(1, 4)))
        private fun `path from 0,0 to 3,2 to 1,4`() = mapOf(
            Position(0, 0) to listOf(Position(3, 2)),
            Position(3, 2) to listOf(Position(1, 4))
        )

        private fun `path from 0,0 to 3,2 to 1,4 to 0,0`() = mapOf(
            Position(0, 0) to listOf(Position(3, 2), Position(0, 4)),
            Position(3, 2) to listOf(Position(1, 4))
        )
    }
}
