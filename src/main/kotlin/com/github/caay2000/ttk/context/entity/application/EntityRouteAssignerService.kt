package com.github.caay2000.ttk.context.entity.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.InvalidRouteEntityException
import com.github.caay2000.ttk.context.entity.domain.Route.Companion.create
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.shared.EntityId

class EntityRouteAssignerService(
    entityRepository: EntityRepository,
    eventPublisher: EventPublisher
) {

    private val entityService: EntityServiceApi = entityService(entityRepository, eventPublisher)

    fun invoke(entityId: EntityId, stops: List<Position>): Either<EntityException, Entity> =
        entityService.findById(entityId)
            .flatMap { entity -> entity.updateRoute(stops) }
            .flatMap { entity -> entityService.save(entity) }
            .flatMap { entity -> entityService.publishEvents(entity) }

    private fun Entity.updateRoute(stops: List<Position>): Either<EntityException, Entity> =
        Either.catch { create(stops) }
            .map { route -> assignRoute(route) }
            .mapLeft { InvalidRouteEntityException(stops) }
}
