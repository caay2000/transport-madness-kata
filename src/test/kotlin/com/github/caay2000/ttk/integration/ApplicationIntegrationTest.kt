package com.github.caay2000.ttk.integration

import com.github.caay2000.ttk.application.Application
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.WorldProvider
import com.github.caay2000.ttk.infra.provider.DefaultWorldProvider
import com.github.caay2000.ttk.mother.ConfigurationMother
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ApplicationIntegrationTest {

    private val configuration = ConfigurationMother.random(worldWidth = 4, worldHeight = 6)
    private val provider: WorldProvider = DefaultWorldProvider()

    @ParameterizedTest
    @MethodSource("examples")
    fun `asaas`(startPosition: Position, destination: Position, turns: Int) {

        val sut = Application(configuration, provider)

        assertThat(sut.invoke(startPosition, destination)).isEqualTo(turns)
    }

    companion object {
        @JvmStatic
        fun examples(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(Position(0, 0), Position(3, 2), 5),
                Arguments.of(Position(0, 0), Position(0, 4), 4),
            )
        }
    }
}
