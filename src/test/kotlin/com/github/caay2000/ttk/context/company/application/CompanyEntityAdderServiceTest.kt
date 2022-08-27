package com.github.caay2000.ttk.context.company.application

import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.secondary.InMemoryCompanyRepository
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import com.github.caay2000.ttk.mother.CompanyMother
import com.github.caay2000.ttk.mother.EntityMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class CompanyEntityAdderServiceTest {

    private val companyRepository: CompanyRepository = InMemoryCompanyRepository(InMemoryDatabase())
    private val eventPublisher: EventPublisher<Event> = mock()
    private val sut = CompanyEntityAdderService(companyRepository, eventPublisher)

    @Test
    fun `company is created correctly`() {

        `company exists`()

        sut.invoke(companyId = company.id, entityId = entity.id).shouldBeRight { company ->
            Assertions.assertThat(company).isEqualTo(expectedCompany)
        }
    }

    private fun `company exists`() {
        companyRepository.save(company)
    }

    private val entity = EntityMother.random()
    private val company = CompanyMother.random()
    private val expectedCompany = company.copy(entities = company.entities + entity.id)
}
