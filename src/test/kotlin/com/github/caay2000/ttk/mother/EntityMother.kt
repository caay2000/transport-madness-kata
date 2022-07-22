package com.github.caay2000.ttk.mother

import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId

object EntityMother {

    fun random(
        id: EntityId = randomDomainId(),
        x: Int = 0,
        y: Int = 0
    ) = Entity.create(
        id = id,
        x = x,
        y = y
    )
}
