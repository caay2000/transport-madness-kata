package com.github.caay2000.ttk.context.company.domain

import com.github.caay2000.ttk.shared.Aggregate
import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId

data class Company(
    override val id: CompanyId,
    val name: String,
    val entities: Set<EntityId>
) : Aggregate() {

    companion object {
        fun create(name: String): Company = Company(
            id = randomDomainId(),
            name = name,
            entities = emptySet()
        )
    }

    fun addEntity(entityId: EntityId): Company =
        copy(entities = entities + entityId)

    fun update() = this
}
