package com.github.caay2000.ttk.context.company.application

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.api.event.CommandBus
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.company.domain.CompanyException
import com.github.caay2000.ttk.context.company.domain.UnknownCompanyException
import com.github.caay2000.ttk.context.entity.primary.command.UpdateAllCompanyVehiclesCommand

class AllCompaniesUpdaterService(
    private val companyRepository: CompanyRepository,
    private val commandBus: CommandBus,
    private val eventPublisher: EventPublisher
) {

    fun invoke(): Either<CompanyException, Unit> =
        findAllCompanies()
            .flatMap { companies -> companies.updateAll() }
            .void()

    private fun List<Company>.updateAll(): Either<CompanyException, Unit> =
        Either.catch { forEach { it.updateCompany().bind() } }
            .mapLeft { UnknownCompanyException(it) }

    private fun Company.updateCompany(): Either<CompanyException, Company> =
        updateCompanyVehicles()
            .map { company -> company.update() }
            .flatMap { company -> company.save() }
            .flatMap { company -> company.publishEvents() }

    private fun Company.updateCompanyVehicles(): Either<CompanyException, Company> =
        commandBus.publish(UpdateAllCompanyVehiclesCommand(this.id))
            .let { this.right() }

    private fun findAllCompanies(): Either<CompanyException, List<Company>> =
        companyRepository.findAll()
            .mapLeft { UnknownCompanyException(it) }

    private fun Company.save(): Either<CompanyException, Company> =
        companyRepository.save(this)
            .mapLeft { UnknownCompanyException(it) }

    private fun Company.publishEvents(): Either<CompanyException, Company> =
        Either.catch { eventPublisher.publish(this.pullEvents()).let { this } }
            .mapLeft { UnknownCompanyException(it) }
}
