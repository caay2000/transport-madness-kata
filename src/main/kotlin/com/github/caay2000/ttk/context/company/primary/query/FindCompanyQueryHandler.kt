package com.github.caay2000.ttk.context.company.primary.query

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryHandler
import com.github.caay2000.ttk.api.event.QueryResponse
import com.github.caay2000.ttk.context.company.application.CompanyFinder
import com.github.caay2000.ttk.context.company.application.CompanyRepository
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.shared.CompanyId
import java.util.UUID

class FindCompanyQueryHandler(companyRepository: CompanyRepository) : QueryHandler<FindCompanyQuery, FindCompanyQueryResponse> {

    private val companyFinder = CompanyFinder(companyRepository)

    override fun handle(query: FindCompanyQuery): FindCompanyQueryResponse =
        companyFinder.invoke(CompanyRepository.FindCompanyCriteria.ById(query.companyId))
            .map { company -> FindCompanyQueryResponse(company) }
            .bind()
}

data class FindCompanyQuery(val companyId: CompanyId) : Query {
    override val queryId: UUID = UUID.randomUUID()
}

data class FindCompanyQueryResponse(override val value: Company) : QueryResponse
