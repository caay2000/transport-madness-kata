package com.github.caay2000.ttk.mother

import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId

object EntityMother {

    fun random(
        id: EntityId = randomDomainId(),
        currentPosition: Position = Position(0, 0),
        destination: Position = Position(0, 0)
    ) = Entity(
        id = id,
        currentPosition = currentPosition,
        destination = destination
    )
}
