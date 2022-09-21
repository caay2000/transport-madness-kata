package com.github.caay2000.ttk.context.company.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.company.domain.CompanyException
import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.EntityId

class CompanyEntityAdderService(companyRepository: CompanyRepository, eventPublisher: EventPublisher) {

    private val companyService: CompanyServiceApi = companyService(companyRepository, eventPublisher)

    fun invoke(companyId: CompanyId, entityId: EntityId): Either<CompanyException, Company> =
        companyService.find(companyId)
            .map { company -> company.addEntity(entityId) }
            .flatMap { company -> companyService.save(company) }
            .flatMap { company -> companyService.publishEvents(company) }
}
