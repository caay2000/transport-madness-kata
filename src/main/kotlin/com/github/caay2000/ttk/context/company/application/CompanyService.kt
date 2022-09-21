package com.github.caay2000.ttk.context.company.application

import arrow.core.Either
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.company.domain.CompanyException
import com.github.caay2000.ttk.context.company.domain.UnknownCompanyException
import com.github.caay2000.ttk.shared.CompanyId

interface CompanyServiceApi {

    fun find(id: CompanyId): Either<CompanyException, Company>
    fun findAll(): Either<CompanyException, List<Company>>
    fun save(company: Company): Either<CompanyException, Company>
    fun publishEvents(company: Company): Either<CompanyException, Company>
}

fun companyService(companyRepository: CompanyRepository, eventPublisher: EventPublisher) = object : CompanyServiceApi {

    private val companyFinder = CompanyFinder(companyRepository)

    override fun find(id: CompanyId): Either<CompanyException, Company> =
        companyFinder.invoke(CompanyRepository.FindCompanyCriteria.ById(id))

    override fun findAll(): Either<CompanyException, List<Company>> =
        companyRepository.findAll()
            .mapLeft { UnknownCompanyException(it) }

    override fun save(company: Company): Either<CompanyException, Company> =
        companyRepository.save(company)
            .map { company }
            .mapLeft { UnknownCompanyException(it) }

    override fun publishEvents(company: Company): Either<CompanyException, Company> =
        Either.catch { eventPublisher.publish(company.pullEvents()).let { company } }
            .mapLeft { UnknownCompanyException(it) }
}
