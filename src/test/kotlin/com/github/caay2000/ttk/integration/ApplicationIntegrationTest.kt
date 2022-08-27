package com.github.caay2000.ttk.integration

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.ApplicationService
import com.github.caay2000.ttk.ApplicationServiceApi
import com.github.caay2000.ttk.context.entity.domain.EntityType
import com.github.caay2000.ttk.context.entity.domain.PassengerTrain
import com.github.caay2000.ttk.context.entity.domain.Railcar
import com.github.caay2000.ttk.context.location.application.LocationRepository.FindLocationCriteria.ByPositionCriteria
import com.github.caay2000.ttk.context.location.secondary.InMemoryLocationRepository
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.secondary.InMemoryWorldRepository
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import com.github.caay2000.ttk.mother.ConfigurationMother
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ApplicationIntegrationTest {

    private val inMemoryDatabase = InMemoryDatabase()

    @ParameterizedTest
    @MethodSource("exercise 3 data")
    fun `exercise 3`(startPosition: Position, paths: Map<Position, List<Position>>, route: List<Position>, finishingTurn: Int) {

        val sut: ApplicationServiceApi = ApplicationService(inMemoryDatabase)
        sut.setConfiguration(ConfigurationMother.random(worldWidth = 4, worldHeight = 6, minDistanceBetweenCities = 1))

        val locationA = Position(0, 0)
        val locationB = Position(3, 0)
        val locationC = Position(3, 2)
        val locationD = Position(1, 4)

        sut.createWorld()
        sut.createLocation("A", locationA, 500)
        sut.createLocation("B", locationB, 100)
        sut.createLocation("C", locationC, 1000)
        sut.createLocation("D", locationD, 250)

        paths.forEach { (source, destinations) ->
            destinations.forEach { destination -> sut.createConnection(source, destination) }
        }

        val companyA = sut.createCompany("company A")
        val entityA = sut.createEntity(companyA.id, PassengerTrain(3), startPosition)
        sut.assignRoute(entityA.id, route)

        repeat(finishingTurn) {
            sut.passTurn()
        }

        assertThat(getWorld().currentTurn).isEqualTo(finishingTurn)
    }

    @Test
    fun `exercise 5`() {

        val finishingTurn = 18

        val sut: ApplicationServiceApi = ApplicationService(inMemoryDatabase)
        sut.setConfiguration(ConfigurationMother.random(worldWidth = 4, worldHeight = 6, minDistanceBetweenCities = 1))

        val locationA = Position(0, 0)
        val locationB = Position(3, 2)
        val locationC = Position(1, 4)

        sut.createWorld()
        sut.createLocation("A", locationA, 500)
        sut.createLocation("B", locationB, 1000)
        sut.createLocation("C", locationC, 250)

        sut.createConnection(locationA, locationB)
        sut.createConnection(locationB, locationC)
        sut.createConnection(locationC, locationA)

        val companyA = sut.createCompany("company A")
        val entityA = sut.createEntity(companyA.id, PassengerTrain(3), locationA)
        sut.assignRoute(entityA.id, listOf(locationA, locationB, locationC, locationB))

        repeat(finishingTurn) {
            sut.passTurn()
        }

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

        val sut: ApplicationServiceApi = ApplicationService(inMemoryDatabase)
        sut.setConfiguration(ConfigurationMother.random(worldWidth = 4, worldHeight = 6, minDistanceBetweenCities = 1))

        val locationA = Position(0, 0)
        val locationB = Position(3, 0)

        sut.createWorld()
        sut.createLocation("A", locationA, 5000)
        sut.createLocation("B", locationB, 5000)

        sut.createConnection(locationA, locationB)

        val companyA = sut.createCompany("company A")
        val entityA = sut.createEntity(companyA.id, entityType, locationA)
        sut.assignRoute(entityA.id, listOf(locationA, locationB))

        repeat(finishingTurn) {
            sut.passTurn()
        }

        ConfigurationMother.random(worldWidth = 4, worldHeight = 6, minDistanceBetweenCities = 1)
    }

    @Test
    fun `exercise 7 with service api`() {

        val finishingTurn = 186

        val sut: ApplicationServiceApi = ApplicationService(inMemoryDatabase)
        sut.setConfiguration(ConfigurationMother.random(worldWidth = 40, worldHeight = 40))

        val locationA = Position(8, 4)
        val locationB = Position(5, 25)
        val locationC = Position(20, 25)
        val locationD = Position(35, 5)
        val locationE = Position(35, 35)

        sut.createWorld()
        sut.createLocation("A", locationA, 500)
        sut.createLocation("B", locationB, 1000)
        sut.createLocation("C", locationC, 250)
        sut.createLocation("D", locationD, 500)
        sut.createLocation("E", locationE, 750)

        sut.createConnection(locationA, locationB)
        sut.createConnection(locationA, locationC)
        sut.createConnection(locationC, locationE)
        sut.createConnection(locationC, locationD)

        val companyA = sut.createCompany("company A")
        val entityA = sut.createEntity(companyA.id, PassengerTrain(3), locationA)
        sut.assignRoute(entityA.id, listOf(locationA, locationB, locationA, locationC, locationD, locationC, locationE, locationC))

        repeat(finishingTurn) {
            sut.passTurn()
        }

        sut.printWorld()

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

    private fun getWorld() =
        InMemoryWorldRepository(inMemoryDatabase).get().bind()

    private fun getLocation(position: Position) =
        InMemoryLocationRepository(inMemoryDatabase).find(ByPositionCriteria(position)).bind()
}
