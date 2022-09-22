package com.github.caay2000.ttk.context.company.application

import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.secondary.InMemoryCompanyRepository
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class CompanyCreatorServiceTest {

    private val companyRepository: CompanyRepository = InMemoryCompanyRepository(InMemoryDatabase())
    private val eventPublisher: EventPublisher = mock()
    private val sut = CompanyCreatorService(companyRepository, eventPublisher)

    @Test
    fun `company is created correctly`() {

        sut.invoke(expectedName).shouldBeRight { company ->
            assertThat(company.name).isEqualTo(expectedName)
        }
    }

    private val expectedName = "companyName"
}
