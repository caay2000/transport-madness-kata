package com.github.caay2000.ttk.mother

import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityStatus
import com.github.caay2000.ttk.context.entity.domain.EntityType
import com.github.caay2000.ttk.context.entity.domain.PassengerTrain
import com.github.caay2000.ttk.context.entity.domain.Route
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId

object EntityMother {

    fun random(
        id: EntityId = randomDomainId(),
        companyId: CompanyId = randomDomainId(),
        entityType: EntityType = PassengerTrain(),
        currentPosition: Position = Position(0, 0),
        currentDuration: Int = 0,
        route: Route = Route(listOf(currentPosition)),
        status: EntityStatus = EntityStatus.STOP,
        pax: Int = 0
    ) = Entity(
        id = id,
        companyId = companyId,
        entityType = entityType,
        currentPosition = currentPosition,
        currentDuration = currentDuration,
        route = route,
        status = status,
        pax = pax
    )
}
