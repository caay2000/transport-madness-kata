package com.github.caay2000.ttk.context.company.application

import arrow.core.Either
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.company.domain.CompanyException
import com.github.caay2000.ttk.context.company.domain.UnknownCompanyException

class CompanyFinder(private val companyRepository: CompanyRepository) {

    fun invoke(criteria: CompanyRepository.FindCompanyCriteria): Either<CompanyException, Company> =
        companyRepository.find(criteria)
            .mapLeft { UnknownCompanyException(it) }
}
