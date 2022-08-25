package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.application.EntityRepository
import com.github.caay2000.ttk.context.entity.application.EntityService
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityUpdateMoverServiceException
import com.github.caay2000.ttk.context.entity.domain.update.EntityMovementStrategy
import com.github.caay2000.ttk.context.world.application.WorldRepository

class EntityUpdateMoverService(
    worldRepository: WorldRepository,
    entityRepository: EntityRepository,
    eventPublisher: EventPublisher<Event>
) : EntityService(entityRepository, eventPublisher) {

    private val entityMovementStrategy = EntityMovementStrategy.SimpleEntityMovementStrategy(worldRepository)

    fun invoke(initialEntity: Entity): Either<EntityException, Entity> =
        Either.catch { initialEntity.updateMove(entityMovementStrategy) }
            .mapLeft { EntityUpdateMoverServiceException(it) }
}
