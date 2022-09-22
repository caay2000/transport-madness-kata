package com.github.caay2000.ttk.integration

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.ApplicationService
import com.github.caay2000.ttk.ApplicationServiceApi
import com.github.caay2000.ttk.context.entity.domain.PassengerTrain
import com.github.caay2000.ttk.context.entity.secondary.InMemoryEntityRepository
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.secondary.InMemoryWorldRepository
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import com.github.caay2000.ttk.mother.ConfigurationMother
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class Exercise8IntegrationTest {

    private val inMemoryDatabase = InMemoryDatabase()

    @Test
    fun `exercise 8 with service api`() {

        val sut: ApplicationServiceApi = ApplicationService(inMemoryDatabase)
        sut.setConfiguration(ConfigurationMother.random(worldWidth = 10, worldHeight = 10))

        sut.createWorld()
        sut.createLocation("A", locationA, 500)
        sut.createLocation("B", locationB, 1000)
        sut.createLocation("C", locationC, 250)
        sut.createLocation("D", locationD, 250)

        val companyA = sut.createCompany("companyA")
        val companyB = sut.createCompany("companyB")
        sut.createConnection(companyA.id, locationA, locationB)
        sut.createConnection(companyB.id, locationA, locationC)
        sut.createConnection(companyB.id, locationC, locationD)
        sut.createConnection(companyB.id, locationD, locationB)

        val entity = sut.createEntity(companyB.id, PassengerTrain(3), locationA)
        sut.assignRoute(entity.id, listOf(locationA, locationB))

        while (notFinished()) {
            sut.passTurn()
        }
        sut.printWorld()

        Assertions.assertThat(getWorld().currentTurn).isEqualTo(44)
    }

    private fun notFinished(): Boolean =
        (getWorld().currentTurn > 1 && getEntity().currentPosition == locationA).not()

    private val locationA = Position(1, 1)
    private val locationB = Position(8, 1)
    private val locationD = Position(8, 8)
    private val locationC = Position(1, 8)

    private fun getWorld() = InMemoryWorldRepository(inMemoryDatabase).get().bind()
    private fun getEntity() = InMemoryEntityRepository(inMemoryDatabase).findAll().bind().first()
}
