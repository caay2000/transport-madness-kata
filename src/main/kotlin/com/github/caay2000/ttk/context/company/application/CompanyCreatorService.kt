package com.github.caay2000.ttk.context.company.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.company.domain.CompanyException
import com.github.caay2000.ttk.context.company.domain.UnknownCompanyException

class CompanyCreatorService(
    private val companyRepository: CompanyRepository,
    private val eventPublisher: EventPublisher
) {

    fun invoke(name: String) =
        createCompany(name)
            .flatMap { company -> company.save() }
            .flatMap { company -> company.publishEvents() }

    private fun createCompany(name: String): Either<CompanyException, Company> =
        Either.catch { Company.create(name) }
            .mapLeft { UnknownCompanyException(it) }

    private fun Company.save(): Either<CompanyException, Company> =
        companyRepository.save(this)
            .map { this }
            .mapLeft { UnknownCompanyException(it) }

    private fun Company.publishEvents(): Either<CompanyException, Company> =
        Either.catch { eventPublisher.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownCompanyException(it) }
}
