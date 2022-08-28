package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityUpdateMoverServiceException
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException
import com.github.caay2000.ttk.context.entity.domain.update.EntityMovementStrategy
import com.github.caay2000.ttk.context.world.application.WorldRepository

class EntityUpdateMoverService(
    worldRepository: WorldRepository,
    private val eventPublisher: EventPublisher
) {

    private val entityMovementStrategy = EntityMovementStrategy.SimpleEntityMovementStrategy(worldRepository)

    fun invoke(initialEntity: Entity): Either<EntityException, Entity> =
        Either.catch { initialEntity.updateMove(entityMovementStrategy) }
            .mapLeft { EntityUpdateMoverServiceException(it) }
            .flatMap { updatedEntity -> updatedEntity.publishEvents() }

    private fun Entity.publishEvents(): Either<EntityException, Entity> =
        Either.catch { eventPublisher.publish(pullEvents()).let { this } }
            .mapLeft { UnknownEntityException(it) }
}
