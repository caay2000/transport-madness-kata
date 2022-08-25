package com.github.caay2000.ttk.context.company.application

import arrow.core.Either
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.company.domain.CompanyException
import com.github.caay2000.ttk.context.company.domain.UnknownCompanyException

abstract class CompanyService(
    private val companyRepository: CompanyRepository,
    private val eventBus: EventPublisher<Event>
) {

    protected fun Company.save(): Either<CompanyException, Company> =
        companyRepository.save(this)
            .map { this }
            .mapLeft { UnknownCompanyException(it) }

    protected fun Company.publishEvents(): Either<CompanyException, Company> =
        Either.catch { eventBus.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownCompanyException(it) }
}
