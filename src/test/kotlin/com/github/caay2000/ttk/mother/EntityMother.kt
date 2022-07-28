package com.github.caay2000.ttk.mother

import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.entity.EntityStatus
import com.github.caay2000.ttk.domain.entity.Route
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId

object EntityMother {

    fun random(
        id: EntityId = randomDomainId(),
        currentPosition: Position = Position(0, 0),
        currentDuration: Int = 0,
        route: Route = Route(listOf(currentPosition)),
        status: EntityStatus = EntityStatus.STOP,
        configuration: Configuration = ConfigurationMother.random()
    ) = Entity(
        id = id,
        currentPosition = currentPosition,
        currentDuration = currentDuration,
        route = route,
        status = status,
        configuration = configuration
    )
}
