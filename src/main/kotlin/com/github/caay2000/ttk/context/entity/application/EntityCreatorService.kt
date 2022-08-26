package com.github.caay2000.ttk.context.entity.application

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.application.CompanyRepository
import com.github.caay2000.ttk.context.company.application.CompanyRepository.FindCompanyCriteria.ByIdCriteria
import com.github.caay2000.ttk.context.entity.domain.CompanyNotFound
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityType
import com.github.caay2000.ttk.context.entity.domain.InvalidEntityPositionException
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException
import com.github.caay2000.ttk.context.world.application.WorldRepository
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.shared.CompanyId

class EntityCreatorService(
    private val worldRepository: WorldRepository,
    private val companyRepository: CompanyRepository,
    private val entityRepository: EntityRepository,
    private val eventPublisher: EventPublisher<Event>
) {

    fun invoke(companyId: CompanyId, entityType: EntityType, position: Position): Either<EntityException, Entity> =
        guardCompanyExists(companyId)
            .flatMap { findWorld() }
            .flatMap { world -> world.guardPosition(position) }
            .flatMap { createEntity(companyId = companyId, entityType = entityType, position = position) }
            .flatMap { entity -> entity.save() }
            .flatMap { entity -> entity.publishEvents() }

    private fun guardCompanyExists(companyId: CompanyId): Either<EntityException, Unit> =
        if (companyRepository.exists(ByIdCriteria(companyId))) Unit.right()
        else CompanyNotFound(companyId).left()

    private fun findWorld(): Either<EntityException, World> =
        worldRepository.get()
            .mapLeft { UnknownEntityException(it) }

    private fun createEntity(companyId: CompanyId, entityType: EntityType, position: Position): Either<EntityException, Entity> =
        Either.catch { Entity.create(companyId = companyId, entityType = entityType, position = position) }
            .mapLeft { UnknownEntityException(it) }

    private fun World.guardPosition(position: Position): Either<EntityException, World> =
        Either.catch { getCell(position) }
            .map { this }
            .mapLeft { InvalidEntityPositionException(position) }

    private fun Entity.save(): Either<EntityException, Entity> =
        entityRepository.save(this)
            .map { this }
            .mapLeft { UnknownEntityException(it) }

    private fun Entity.publishEvents(): Either<EntityException, Entity> =
        Either.catch { eventPublisher.publish(pullEvents()).let { this } }
            .mapLeft { UnknownEntityException(it) }
}
