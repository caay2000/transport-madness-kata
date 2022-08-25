package com.github.caay2000.ttk.context.company.application

import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.secondary.InMemoryCompanyRepository
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class CompanyCreatorServiceTest {

    private val companyRepository: CompanyRepository = InMemoryCompanyRepository(InMemoryDatabase())
    private val eventPublisher: EventPublisher<Event> = mock()
    private val sut = CompanyCreatorService(companyRepository, eventPublisher)

    @Test
    fun `company is created correctly`() {

//        `world exists`()

        sut.invoke(expectedName).shouldBeRight { company ->
            assertThat(company.name).isEqualTo(expectedName)
        }
    }

    private val expectedName = "companyName"

//    private fun `world exists`() = provider.set(WorldMother.random())
}
