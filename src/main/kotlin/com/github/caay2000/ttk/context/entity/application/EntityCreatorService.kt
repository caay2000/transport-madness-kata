package com.github.caay2000.ttk.context.entity.application

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.application.CompanyRepository
import com.github.caay2000.ttk.context.company.application.CompanyRepository.FindCompanyCriteria.ById
import com.github.caay2000.ttk.context.entity.domain.CompanyNotFoundEntityException
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityType
import com.github.caay2000.ttk.context.entity.domain.InvalidEntityPositionEntityException
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException
import com.github.caay2000.ttk.context.world.application.WorldRepository
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.shared.CompanyId

class EntityCreatorService(
    private val worldRepository: WorldRepository,
    private val companyRepository: CompanyRepository,
    entityRepository: EntityRepository,
    eventPublisher: EventPublisher
) {

    private val entityService: EntityServiceApi = entityService(entityRepository, eventPublisher)

    fun invoke(companyId: CompanyId, entityType: EntityType, position: Position): Either<EntityException, Entity> =
        guardCompanyExists(companyId)
            .flatMap { findWorld() }
            .flatMap { world -> world.guardPosition(position) }
            .flatMap { createEntity(companyId = companyId, entityType = entityType, position = position) }
            .flatMap { entity -> entityService.save(entity) }
            .flatMap { entity -> entityService.publishEvents(entity) }

    private fun guardCompanyExists(companyId: CompanyId): Either<EntityException, Unit> =
        if (companyRepository.exists(ById(companyId))) Unit.right()
        else CompanyNotFoundEntityException(companyId).left()

    private fun findWorld(): Either<EntityException, World> =
        worldRepository.get()
            .mapLeft { UnknownEntityException(it) }

    private fun createEntity(companyId: CompanyId, entityType: EntityType, position: Position): Either<EntityException, Entity> =
        Either.catch { Entity.create(companyId = companyId, entityType = entityType, position = position) }
            .mapLeft { UnknownEntityException(it) }

    private fun World.guardPosition(position: Position): Either<EntityException, World> =
        Either.catch { getCell(position) }
            .map { this }
            .mapLeft { InvalidEntityPositionEntityException(position) }
}
