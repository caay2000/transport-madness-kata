package com.github.caay2000.ttk.context.company.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.application.CompanyRepository.FindCompanyCriteria.ByIdCriteria
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.company.domain.CompanyException
import com.github.caay2000.ttk.context.company.domain.UnknownCompanyException
import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.EntityId

class CompanyEntityAdderService(
    private val companyRepository: CompanyRepository,
    private val eventPublisher: EventPublisher<Event>
) {

    fun invoke(companyId: CompanyId, entityId: EntityId): Either<CompanyException, Company> =
        findCompany(companyId)
            .map { company -> company.addEntity(entityId) }
            .flatMap { company -> company.save() }
            .flatMap { company -> company.publishEvents() }

    private fun findCompany(companyId: CompanyId): Either<CompanyException, Company> =
        companyRepository.find(ByIdCriteria(companyId))
            .mapLeft { UnknownCompanyException(it) }

    private fun Company.save(): Either<CompanyException, Company> =
        companyRepository.save(this)
            .map { this }
            .mapLeft { UnknownCompanyException(it) }

    private fun Company.publishEvents(): Either<CompanyException, Company> =
        Either.catch { eventPublisher.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownCompanyException(it) }
}
