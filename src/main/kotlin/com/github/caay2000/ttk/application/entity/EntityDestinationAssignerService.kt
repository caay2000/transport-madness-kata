package com.github.caay2000.ttk.application.entity

import arrow.core.Either
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.domain.world.WorldProvider
import com.github.caay2000.ttk.shared.EntityId

class EntityDestinationAssignerService(private val worldProvider: WorldProvider) {

    fun invoke(entityId: EntityId, position: Position): Either<EntityException, World> =
        TODO()
}
