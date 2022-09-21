package com.github.caay2000.ttk.integration

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.ApplicationService
import com.github.caay2000.ttk.ApplicationServiceApi
import com.github.caay2000.ttk.context.entity.domain.PassengerTrain
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.secondary.InMemoryWorldRepository
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import com.github.caay2000.ttk.mother.ConfigurationMother
import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class Exercise3IntegrationTest {

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

        val company = sut.createCompany("company")
        paths.forEach { (source, destinations) ->
            destinations.forEach { destination -> sut.createConnection(company.id, source, destination) }
        }

        val entityA = sut.createEntity(company.id, PassengerTrain(3), startPosition)
        sut.assignRoute(entityA.id, route)

        repeat(finishingTurn) {
            sut.passTurn()
        }

        Assertions.assertThat(getWorld().currentTurn).isEqualTo(finishingTurn)
    }

    private fun getWorld() = InMemoryWorldRepository(inMemoryDatabase).get().bind()

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
