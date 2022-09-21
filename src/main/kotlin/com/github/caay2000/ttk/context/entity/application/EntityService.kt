package com.github.caay2000.ttk.context.entity.application

import arrow.core.Either
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.application.EntityRepository.FindAllCriteria
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityNotFoundEntityException
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException
import com.github.caay2000.ttk.shared.EntityId

interface EntityServiceApi {

    fun findById(id: EntityId): Either<EntityException, Entity>
    fun findAllEntities(criteria: FindAllCriteria): Either<EntityException, List<Entity>>
    fun save(entity: Entity): Either<EntityException, Entity>
    fun publishEvents(entity: Entity): Either<EntityException, Entity>
}

fun entityService(entityRepository: EntityRepository, eventPublisher: EventPublisher) = object : EntityServiceApi {

    override fun findById(id: EntityId): Either<EntityException, Entity> =
        entityRepository.find(EntityRepository.FindEntityCriteria.ByIdCriteria(id))
            .mapLeft { EntityNotFoundEntityException(id) }

    override fun findAllEntities(criteria: FindAllCriteria): Either<EntityException, List<Entity>> =
        entityRepository.findAll(criteria)
            .mapLeft { UnknownEntityException(it) }

    override fun save(entity: Entity): Either<EntityException, Entity> =
        entityRepository.save(entity)
            .map { entity }
            .mapLeft { UnknownEntityException(it) }

    override fun publishEvents(entity: Entity): Either<EntityException, Entity> =
        Either.catch { eventPublisher.publish(entity.pullEvents()).let { entity } }
            .mapLeft { UnknownEntityException(it) }
}
