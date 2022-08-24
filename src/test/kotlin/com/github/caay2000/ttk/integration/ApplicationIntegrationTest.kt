package com.github.caay2000.ttk.integration

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.Application
import com.github.caay2000.ttk.Application.LocationRequest
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.domain.EntityType
import com.github.caay2000.ttk.context.entity.domain.PassengerTrain
import com.github.caay2000.ttk.context.entity.domain.Railcar
import com.github.caay2000.ttk.context.location.application.LocationRepository.FindLocationCriteria.ByPosition
import com.github.caay2000.ttk.context.location.secondary.InMemoryLocationRepository
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
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
    private val inMemoryDatabase = InMemoryDatabase()

    @ParameterizedTest
    @MethodSource("exercise 3 data")
    fun `exercise 3`(startPosition: Position, paths: Map<Position, List<Position>>, route: List<Position>, turns: Int) {

        val sut = Application(configuration, provider, inMemoryDatabase)

        assertThat(
            sut.invoke(
                entityType = PassengerTrain(3),
                startPosition = listOf(startPosition),
                paths = paths,
                locations = setOf(
                    LocationRequest("A", Position(0, 0), 500),
                    LocationRequest("B", Position(3, 0), 100),
                    LocationRequest("C", Position(3, 2), 1000),
                    LocationRequest("D", Position(1, 4), 250)
                ),
                route = route
            )
        ).isEqualTo(turns)
    }

    @Test
    fun `exercise 5`() {

        val finishingTurn = 18

        val sut = Application(configuration, provider, inMemoryDatabase)

        val locationA = Position(0, 0)
        val locationB = Position(3, 2)
        val locationC = Position(1, 4)
        assertThat(
            sut.invoke(
                entityType = PassengerTrain(3),
                startPosition = listOf(locationA),
                paths = `path from 0,0 to 3,2 to 1,4 to 0,0`(),
                locations = setOf(
                    LocationRequest("A", locationA, 500),
                    LocationRequest("B", locationB, 1000),
                    LocationRequest("C", locationC, 250)
                ),
                route = `route from 0,0 to 3,2 to 1,4 to 3,2`()
            )
        ).isEqualTo(finishingTurn)

        assertThat(getLocation(locationA).pax).isEqualTo(17)
        assertThat(getLocation(locationA).received).isEqualTo(16)
        assertThat(getLocation(locationB).pax).isEqualTo(8)
        assertThat(getLocation(locationB).received).isEqualTo(6)
        assertThat(getLocation(locationC).pax).isEqualTo(4)
        assertThat(getLocation(locationC).received).isEqualTo(12)
    }

    @ParameterizedTest
    @MethodSource("exercise 6 data")
    fun `exercise 6`(entityType: EntityType, paxA: Int, receivedA: Int, paxB: Int, receivedB: Int) {

        val finishingTurn = 16

        val sut = Application(configuration, provider, inMemoryDatabase)

        val locationA = Position(0, 0)
        val locationB = Position(3, 0)
        assertThat(
            sut.invoke(
                entityType = entityType,
                startPosition = listOf(locationA),
                paths = `path from 0,0 to 3,0`(),
                locations = setOf(
                    LocationRequest("A", locationA, 5000),
                    LocationRequest("B", locationB, 5000)
                ),
                route = `route from 0,0 to 3,0`(),
                timesToComplete = 2
            )
        ).isEqualTo(finishingTurn)

        assertThat(getLocation(locationA).pax).isEqualTo(paxA)
        assertThat(getLocation(locationA).received).isEqualTo(receivedA)
        assertThat(getLocation(locationB).pax).isEqualTo(paxB)
        assertThat(getLocation(locationB).received).isEqualTo(receivedB)
    }

    @Test
    fun `exercise 7`() {

        val finishingTurn = 186

        val configuration = ConfigurationMother.random(worldWidth = 40, worldHeight = 40)
        val sut = Application(configuration, provider, inMemoryDatabase)

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
                    locationC to listOf(locationE, locationD)
                ),
                locations = setOf(
                    LocationRequest("A", locationA, 500),
                    LocationRequest("B", locationB, 1000),
                    LocationRequest("C", locationC, 250),
                    LocationRequest("D", locationD, 500),
                    LocationRequest("E", locationE, 750)
                ),
                route = listOf(locationA, locationB, locationA, locationC, locationD, locationC, locationE, locationC)
            )
        ).isEqualTo(finishingTurn)

        assertThat(getLocation(locationA).received).isEqualTo(67)
        assertThat(getLocation(locationB).received).isEqualTo(1)
        assertThat(getLocation(locationC).received).isEqualTo(164)
        assertThat(getLocation(locationD).received).isEqualTo(34)
        assertThat(getLocation(locationE).received).isEqualTo(26)
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

    private fun getLocation(position: Position) =
        InMemoryLocationRepository(inMemoryDatabase).find(ByPosition(position)).bind()
}
