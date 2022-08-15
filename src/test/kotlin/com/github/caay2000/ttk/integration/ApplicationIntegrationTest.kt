package com.github.caay2000.ttk.integration

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.Application
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.domain.EntityType
import com.github.caay2000.ttk.context.entity.domain.PassengerTrain
import com.github.caay2000.ttk.context.entity.domain.Railcar
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mother.ConfigurationMother
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ApplicationIntegrationTest {

    private val configuration = ConfigurationMother.random(worldWidth = 4, worldHeight = 6, minDistanceBetweenCities = 1)
    private val provider: Provider = DefaultProvider()

    @ParameterizedTest
    @MethodSource("exercise 3 data")
    fun `exercise 3`(startPosition: Position, paths: Map<Position, List<Position>>, route: List<Position>, turns: Int) {

        val sut = Application(configuration, provider)

        assertThat(
            sut.invoke(
                entityType = PassengerTrain(3),
                startPosition = listOf(startPosition),
                paths = paths,
                locations = setOf(
                    Position(0, 0) to 500,
                    Position(3, 0) to 100,
                    Position(3, 2) to 1000,
                    Position(1, 4) to 250
                ),
                route = route
            )
        ).isEqualTo(turns)
    }

    @Test
    fun `exercise 5`() {

        val finishingTurn = 18

        val sut = Application(configuration, provider)

        val locationA = Position(0, 0)
        val locationB = Position(3, 2)
        val locationC = Position(1, 4)
        assertThat(
            sut.invoke(
                entityType = PassengerTrain(3),
                startPosition = listOf(locationA),
                paths = `path from 0,0 to 3,2 to 1,4 to 0,0`(),
                locations = setOf((locationA to 500), (locationB to 1000), (locationC to 250)),
                route = `route from 0,0 to 3,2 to 1,4 to 3,2`()
            )
        ).isEqualTo(finishingTurn)

        val world = provider.get().bind()
        assertThat(world.getLocation(locationA).pax).isEqualTo(17)
        assertThat(world.getLocation(locationA).received).isEqualTo(16)
        assertThat(world.getLocation(locationB).pax).isEqualTo(8)
        assertThat(world.getLocation(locationB).received).isEqualTo(6)
        assertThat(world.getLocation(locationC).pax).isEqualTo(4)
        assertThat(world.getLocation(locationC).received).isEqualTo(12)
    }

    @ParameterizedTest
    @MethodSource("exercise 6 data")
    fun `exercise 6`(entityType: EntityType, paxA: Int, receivedA: Int, paxB: Int, receivedB: Int) {

        val finishingTurn = 16

        val sut = Application(configuration, provider)

        val locationA = Position(0, 0)
        val locationB = Position(3, 0)
        assertThat(
            sut.invoke(
                entityType = entityType,
                startPosition = listOf(locationA),
                paths = `path from 0,0 to 3,0`(),
                locations = setOf((locationA to 5000), (locationB to 5000)),
                route = `route from 0,0 to 3,0`(),
                timesToComplete = 2
            )
        ).isEqualTo(finishingTurn)

        val world = provider.get().bind()
        assertThat(world.getLocation(locationA).pax).isEqualTo(paxA)
        assertThat(world.getLocation(locationA).received).isEqualTo(receivedA)
        assertThat(world.getLocation(locationB).pax).isEqualTo(paxB)
        assertThat(world.getLocation(locationB).received).isEqualTo(receivedB)
    }

    @Test
    fun `exercise 7`() {

        val finishingTurn = 18

        val configuration = ConfigurationMother.random(worldWidth = 40, worldHeight = 40)
        val sut = Application(configuration, provider)

        val locationA = Position(8, 4)
        val locationB = Position(5, 25)
        val locationC = Position(20, 25)
        val locationD = Position(35, 5)
        val locationE = Position(35, 35)
        assertThat(
            sut.invoke(
                entityType = PassengerTrain(3),
                startPosition = listOf(locationA),
                paths = mapOf(
                    locationA to listOf(locationB, locationC),
                    locationC to listOf(locationD, locationE)
                ),
                locations = setOf(
                    (locationA to 500),
                    (locationB to 1000),
                    (locationC to 250),
                    (locationD to 500),
                    (locationE to 750)
                ),
                route = listOf(locationA, locationB, locationA, locationC, locationD, locationC, locationE, locationC)
            )
        ).isEqualTo(finishingTurn)

        val world = provider.get().bind()
        assertThat(world.getLocation(locationA).pax).isEqualTo(17)
        assertThat(world.getLocation(locationA).received).isEqualTo(16)
        assertThat(world.getLocation(locationB).pax).isEqualTo(8)
        assertThat(world.getLocation(locationB).received).isEqualTo(6)
        assertThat(world.getLocation(locationC).pax).isEqualTo(4)
        assertThat(world.getLocation(locationC).received).isEqualTo(12)
    }

    companion object {
        @JvmStatic
        fun `exercise 3 data`(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(Position(0, 0), `path from 0,0 to 3,0`(), `route from 0,0 to 3,0`(), 8),
                Arguments.of(Position(0, 0), `path from 0,0 to 3,2`(), `route from 0,0 to 3,2`(), 10),
                Arguments.of(Position(0, 0), `path from 0,0 to 1,4`(), `route from 0,0 to 1,4`(), 10),
                Arguments.of(Position(0, 0), `path from 0,0 to 3,2 to 1,4`(), `route from 0,0 to 3,2 to 1,4`(), 17),
                Arguments.of(Position(0, 0), `path from 0,0 to 3,2 to 1,4 to 0,0`(), `route from 0,0 to 3,2 to 1,4`(), 14),
                Arguments.of(Position(0, 0), `path from 0,0 to 3,2 to 1,4 to 0,0`(), `route from 0,0 to 3,2 to 1,4 to 3,2`(), 18)
            )
        }

        @JvmStatic
        fun `exercise 6 data`(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(PassengerTrain(3), 90, 110, 50, 70),
                Arguments.of(Railcar(), 120, 60, 100, 40)
            )
        }

        private fun `route from 0,0 to 3,0`() = listOf(Position(0, 0), Position(3, 0))
        private fun `route from 0,0 to 3,2`() = listOf(Position(0, 0), Position(3, 2))
        private fun `route from 0,0 to 1,4`() = listOf(Position(0, 0), Position(1, 4))
        private fun `route from 0,0 to 3,2 to 1,4`() = listOf(Position(0, 0), Position(3, 2), Position(1, 4))
        private fun `route from 0,0 to 3,2 to 1,4 to 3,2`() = listOf(Position(0, 0), Position(3, 2), Position(1, 4), Position(3, 2))

        private fun `path from 0,0 to 3,0`() = mapOf(Position(0, 0) to listOf(Position(3, 0)))
        private fun `path from 0,0 to 3,2`() = mapOf(Position(0, 0) to listOf(Position(3, 2)))
        private fun `path from 0,0 to 1,4`() = mapOf(Position(0, 0) to listOf(Position(1, 4)))
        private fun `path from 0,0 to 3,2 to 1,4`() = mapOf(
            Position(0, 0) to listOf(Position(3, 2)),
            Position(1, 4) to listOf(Position(3, 2))
        )

        private fun `path from 0,0 to 3,2 to 1,4 to 0,0`() = mapOf(
            Position(0, 0) to listOf(Position(3, 2)),
            Position(1, 4) to listOf(Position(0, 0), Position(3, 2))
        )
    }
}
