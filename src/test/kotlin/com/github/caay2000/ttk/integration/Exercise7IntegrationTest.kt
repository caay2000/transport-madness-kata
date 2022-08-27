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

class Exercise7IntegrationTest {

    private val inMemoryDatabase = InMemoryDatabase()

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

        Assertions.assertThat(getLocation(locationA).received).isEqualTo(67)
        Assertions.assertThat(getLocation(locationB).received).isEqualTo(1)
        Assertions.assertThat(getLocation(locationC).received).isEqualTo(164)
        Assertions.assertThat(getLocation(locationD).received).isEqualTo(34)
        Assertions.assertThat(getLocation(locationE).received).isEqualTo(26)
    }

    private fun getLocation(position: Position) =
        InMemoryLocationRepository(inMemoryDatabase).find(LocationRepository.FindLocationCriteria.ByPositionCriteria(position)).bind()
}
