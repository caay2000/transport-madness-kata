package com.github.caay2000.ttk.mother

import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.randomDomainId

object CompanyMother {

    fun random(
        id: CompanyId = randomDomainId(),
        name: String = StringMother.random(),
        entities: Set<Entity> = emptySet()

    ): Company =
        Company(
            id = id,
            name = name,
            entities = entities
        )
}