package com.github.caay2000.ttk.context.entity.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityType
import com.github.caay2000.ttk.context.entity.domain.InvalidEntityPositionException
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException
import com.github.caay2000.ttk.context.world.application.WorldRepository
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World

class EntityCreatorService(
    private val worldRepository: WorldRepository,
    entityRepository: EntityRepository,
    eventPublisher: EventPublisher<Event>
) : EntityService(entityRepository, eventPublisher) {

    fun invoke(entityType: EntityType, position: Position): Either<EntityException, Entity> =
        findWorld()
            .flatMap { world -> world.guardPosition(position) }
            .flatMap { createEntity(entityType = entityType, position = position) }
            .flatMap { entity -> entity.save() }
            .flatMap { entity -> entity.publishEvents() }

    private fun findWorld(): Either<EntityException, World> =
        worldRepository.get()
            .mapLeft { UnknownEntityException(it) }

    private fun createEntity(entityType: EntityType, position: Position): Either<EntityException, Entity> =
        Either.catch { Entity.create(entityType = entityType, position = position) }
            .mapLeft { UnknownEntityException(it) }

    private fun World.guardPosition(position: Position): Either<EntityException, World> =
        Either.catch { getCell(position) }
            .map { this }
            .mapLeft { InvalidEntityPositionException(position) }
}
