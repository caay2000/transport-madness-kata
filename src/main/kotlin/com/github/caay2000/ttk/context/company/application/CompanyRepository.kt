package com.github.caay2000.ttk.context.company.application

import arrow.core.Either
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.shared.CompanyId

interface CompanyRepository {

    fun exists(criteria: FindCompanyCriteria): Boolean
    fun find(criteria: FindCompanyCriteria): Either<Throwable, Company>
    fun findAll(): Either<Throwable, List<Company>>
    fun save(company: Company): Either<Throwable, Company>

    sealed class FindCompanyCriteria {

        data class ByIdCriteria(val id: CompanyId) : FindCompanyCriteria()
    }
}
