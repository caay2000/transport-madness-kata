package com.github.caay2000.ttk.context.company.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.company.domain.CompanyException
import com.github.caay2000.ttk.context.company.domain.UnknownCompanyException

class CompanyCreatorService(provider: Provider, eventPublisher: EventPublisher<Event>) : CompanyService(provider, eventPublisher) {

    fun invoke(name: String) =
        createCompany(name)
            .flatMap { company -> company.save() }
            .flatMap { company -> company.publishEvents() }

    private fun createCompany(name: String): Either<CompanyException, Company> =
        Either.catch { Company.create(name) }
            .mapLeft { UnknownCompanyException(it) }
}
