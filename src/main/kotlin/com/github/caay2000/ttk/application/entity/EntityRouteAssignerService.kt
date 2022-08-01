package com.github.caay2000.ttk.application.entity

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.entity.Route
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.shared.EntityId

class EntityRouteAssignerService(provider: Provider) : EntityService(provider) {

    fun invoke(entityId: EntityId, stops: List<Position>): Either<EntityException, World> =
        findWorld()
            .flatMap { world -> world.updateEntityRoute(entityId, stops.toList()) }
            .flatMap { world -> world.save() }

    private fun World.updateEntityRoute(entityId: EntityId, stops: List<Position>): Either<EntityException, World> =
        findEntity(entityId)
            .flatMap { entity -> entity.updateRoute(stops) }
            .map { entity -> putEntity(entity) }

    private fun Entity.updateRoute(stops: List<Position>): Either<EntityException, Entity> =
        Either.catch { Route.create(stops) }
            .map { route -> assignRoute(route) }
            .mapLeft { InvalidRouteException(stops) }
}
