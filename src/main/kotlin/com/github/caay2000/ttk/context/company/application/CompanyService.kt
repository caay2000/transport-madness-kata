package com.github.caay2000.ttk.context.company.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.company.domain.CompanyException
import com.github.caay2000.ttk.context.company.domain.UnknownCompanyException
import com.github.caay2000.ttk.context.world.domain.World

abstract class CompanyService(
    private val provider: Provider,
    private val eventBus: EventPublisher<Event>
) {

    protected fun findWorld(): Either<CompanyException, World> =
        provider.get()
            .mapLeft { UnknownCompanyException(it) }

    protected fun Company.save(): Either<CompanyException, Company> =
        provider.get()
            .map { world -> world.addCompany(this) }
            .flatMap { world -> provider.set(world) }
            .map { this }
            .mapLeft { UnknownCompanyException(it) }

    protected fun Company.publishEvents(): Either<CompanyException, Company> =
        Either.catch { eventBus.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownCompanyException(it) }
}
