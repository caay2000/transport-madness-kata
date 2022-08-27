package com.github.caay2000.ttk.integration

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.ApplicationService
import com.github.caay2000.ttk.ApplicationServiceApi
import com.github.caay2000.ttk.context.entity.domain.EntityType
import com.github.caay2000.ttk.context.entity.domain.PassengerTrain
import com.github.caay2000.ttk.context.entity.domain.Railcar
import com.github.caay2000.ttk.context.location.application.LocationRepository
import com.github.caay2000.ttk.context.location.secondary.InMemoryLocationRepository
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import com.github.caay2000.ttk.mother.ConfigurationMother
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class Exercise6IntegrationTest {

    private val inMemoryDatabase = InMemoryDatabase()

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

        assertThat(getLocation(locationA).pax).isEqualTo(paxA)
        assertThat(getLocation(locationA).received).isEqualTo(receivedA)
        assertThat(getLocation(locationB).pax).isEqualTo(paxB)
        assertThat(getLocation(locationB).received).isEqualTo(receivedB)
    }

    private fun getLocation(position: Position) =
        InMemoryLocationRepository(inMemoryDatabase).find(LocationRepository.FindLocationCriteria.ByPositionCriteria(position)).bind()

    companion object {

        @JvmStatic
        fun `exercise 6 data`(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(PassengerTrain(3), 90, 110, 50, 70),
                Arguments.of(Railcar(), 120, 60, 100, 40)
            )
        }
    }
}
