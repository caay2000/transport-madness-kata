package com.github.caay2000.ttk.context.entity.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.InvalidRouteException
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.shared.EntityId

class EntityRouteAssignerService(provider: Provider, eventPublisher: EventPublisher<Event>) : EntityService(provider, eventPublisher) {

    fun invoke(entityId: EntityId, stops: List<Position>): Either<EntityException, Entity> =
        findWorld()
            .flatMap { world -> world.updateEntityRoute(entityId, stops.toList()) }
            .flatMap { entity -> entity.save() }
            .flatMap { entity -> entity.publishEvents() }

    private fun World.updateEntityRoute(entityId: EntityId, stops: List<Position>): Either<EntityException, Entity> =
        findEntity(entityId)
            .flatMap { entity -> entity.updateRoute(stops) }

    private fun Entity.updateRoute(stops: List<Position>): Either<EntityException, Entity> =
        Either.catch { com.github.caay2000.ttk.context.entity.domain.Route.create(stops) }
            .map { route -> assignRoute(route) }
            .mapLeft { InvalidRouteException(stops) }
}
