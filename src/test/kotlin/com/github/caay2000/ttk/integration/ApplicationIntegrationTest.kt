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

    private val configuration = ConfigurationMother.random(worldWidth = 4, worldHeight = 6)
    private val provider: Provider = DefaultProvider()

    @ParameterizedTest
    @MethodSource("exercise 2 data")
    fun `execise 1`(startPosition: Position, route: List<Position>, turns: Int) {

        val sut = Application(configuration, provider)

        assertThat(sut.invoke(startPosition, route)).isEqualTo(turns)
    }

    companion object {
        @JvmStatic
        fun `exercise 2 data`(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(Position(0, 0), listOf(Position(0, 0), Position(3, 2)), 12),
                Arguments.of(Position(0, 0), listOf(Position(0, 0), Position(0, 4)), 10),
                Arguments.of(Position(0, 0), listOf(Position(0, 0), Position(3, 2), Position(1, 4)), 17),
                Arguments.of(Position(0, 0), listOf(Position(0, 0), Position(3, 2), Position(1, 4), Position(3, 2)), 22)
            )
        }
    }
}
