package com.github.caay2000.ttk.context.entity.application

import arrow.core.Either
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.EntityId

interface EntityRepository {

    fun exists(criteria: FindEntityCriteria): Boolean
    fun find(criteria: FindEntityCriteria): Either<Throwable, Entity>
    fun findAll(): Either<Throwable, List<Entity>>
    fun findAll(criteria: FindAllCriteria): Either<Throwable, List<Entity>>
    fun save(entity: Entity): Either<Throwable, Entity>

    sealed class FindEntityCriteria {

        data class ByIdCriteria(val id: EntityId) : FindEntityCriteria()
    }

    sealed class FindAllCriteria {
        data class ByCompanyId(val companyId: CompanyId) : FindAllCriteria()
    }
}
