package com.github.caay2000.ttk.context.entity.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.application.EntityRepository.FindEntityCriteria.ByIdCriteria
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityNotFound
import com.github.caay2000.ttk.context.entity.domain.InvalidRouteException
import com.github.caay2000.ttk.context.entity.domain.Route.Companion.create
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.shared.EntityId

class EntityRouteAssignerService(
    private val entityRepository: EntityRepository,
    private val eventPublisher: EventPublisher<Event>
) {

    fun invoke(entityId: EntityId, stops: List<Position>): Either<EntityException, Entity> =
        findEntity(entityId)
            .flatMap { entity -> entity.updateRoute(stops) }
            .flatMap { entity -> entity.save() }
            .flatMap { entity -> entity.publishEvents() }

    private fun findEntity(entityId: EntityId): Either<EntityException, Entity> =
        entityRepository.find(ByIdCriteria(entityId))
            .mapLeft { EntityNotFound(entityId) }

    private fun Entity.updateRoute(stops: List<Position>): Either<EntityException, Entity> =
        Either.catch { create(stops) }
            .map { route -> assignRoute(route) }
            .mapLeft { InvalidRouteException(stops) }

    private fun Entity.save(): Either<EntityException, Entity> =
        entityRepository.save(this)
            .map { this }
            .mapLeft { UnknownEntityException(it) }

    private fun Entity.publishEvents(): Either<EntityException, Entity> =
        Either.catch { eventPublisher.publish(pullEvents()).let { this } }
            .mapLeft { UnknownEntityException(it) }
}
