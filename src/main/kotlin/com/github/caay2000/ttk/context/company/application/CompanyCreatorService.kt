package com.github.caay2000.ttk.context.company.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.company.domain.CompanyException
import com.github.caay2000.ttk.context.company.domain.UnknownCompanyException

class CompanyCreatorService(companyRepository: CompanyRepository, eventPublisher: EventPublisher) {

    private val companyService: CompanyServiceApi = companyService(companyRepository, eventPublisher)

    fun invoke(name: String) =
        createCompany(name)
            .flatMap { company -> companyService.save(company) }
            .flatMap { company -> companyService.publishEvents(company) }

    private fun createCompany(name: String): Either<CompanyException, Company> =
        Either.catch { Company.create(name) }
            .mapLeft { UnknownCompanyException(it) }
}
