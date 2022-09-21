package com.github.caay2000.ttk.integration

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.ApplicationService
import com.github.caay2000.ttk.ApplicationServiceApi
import com.github.caay2000.ttk.context.entity.domain.PassengerTrain
import com.github.caay2000.ttk.context.location.application.LocationRepository
import com.github.caay2000.ttk.context.location.secondary.InMemoryLocationRepository
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import com.github.caay2000.ttk.mother.ConfigurationMother
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class Exercise5IntegrationTest {

    private val inMemoryDatabase = InMemoryDatabase()

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

        val company = sut.createCompany("company")
        sut.createConnection(company.id, locationA, locationB)
        sut.createConnection(company.id, locationB, locationC)
        sut.createConnection(company.id, locationC, locationA)

        val entity = sut.createEntity(company.id, PassengerTrain(3), locationA)
        sut.assignRoute(entity.id, listOf(locationA, locationB, locationC, locationB))

        repeat(finishingTurn) {
            sut.passTurn()
        }

        Assertions.assertThat(getLocation(locationA).pax).isEqualTo(17)
        Assertions.assertThat(getLocation(locationA).received).isEqualTo(16)
        Assertions.assertThat(getLocation(locationB).pax).isEqualTo(8)
        Assertions.assertThat(getLocation(locationB).received).isEqualTo(6)
        Assertions.assertThat(getLocation(locationC).pax).isEqualTo(4)
        Assertions.assertThat(getLocation(locationC).received).isEqualTo(12)
    }

    private fun getLocation(position: Position) =
        InMemoryLocationRepository(inMemoryDatabase).find(LocationRepository.FindLocationCriteria.ByPosition(position)).bind()
}
