package com.github.caay2000.ttk.context.company.secondary

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.company.application.CompanyRepository
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.infra.database.InMemoryDatabase

class InMemoryCompanyRepository(private val db: InMemoryDatabase) : CompanyRepository {

    override fun exists(criteria: CompanyRepository.FindCompanyCriteria): Boolean =
        when (criteria) {
            is CompanyRepository.FindCompanyCriteria.ByIdCriteria ->
                db.exists(TABLE_NAME, criteria.id.rawId)
        }

    override fun find(criteria: CompanyRepository.FindCompanyCriteria): Either<Throwable, Company> =
        Either.catch {
            when (criteria) {
                is CompanyRepository.FindCompanyCriteria.ByIdCriteria ->
                    db.getById<Company>(TABLE_NAME, criteria.id.rawId)
            }
        }.flatMap { it?.right() ?: NoSuchElementException().left() }

    override fun findAll(): Either<Throwable, List<Company>> =
        Either.catch { db.getAll(TABLE_NAME) }

    override fun save(company: Company): Either<Throwable, Company> =
        Either.catch {
            db.save(TABLE_NAME, company.id.rawId, company)
        }.map { company }

    companion object {
        const val TABLE_NAME = "company"
    }
}
