package com.github.caay2000.ttk.context.entity.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World

class EntityCreatorService(provider: Provider, eventPublisher: EventPublisher<Event>) : EntityService(provider, eventPublisher) {

    fun invoke(position: Position): Either<EntityException, Entity> =
        findWorld()
            .flatMap { world -> world.guardPosition(position) }
            .flatMap { createEntity(position) }
            .flatMap { entity -> entity.save() }
            .flatMap { entity -> entity.publishEvents() }

    private fun createEntity(position: Position): Either<EntityException, Entity> =
        provider.getConfiguration()
            .map { configuration -> Entity.create(position = position, configuration = configuration) }
            .mapLeft { UnknownEntityException(it) }

    private fun World.guardPosition(position: Position): Either<EntityException, World> =
        Either.catch { getCell(position) }
            .map { this }
            .mapLeft { InvalidEntityPositionException(position) }
}
