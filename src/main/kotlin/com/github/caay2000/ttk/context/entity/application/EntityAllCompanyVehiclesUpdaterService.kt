package com.github.caay2000.ttk.context.entity.application

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.application.EntityRepository.FindAllCriteria.ByCompanyId
import com.github.caay2000.ttk.context.entity.application.update.EntityUpdateLoaderService
import com.github.caay2000.ttk.context.entity.application.update.EntityUpdateMoverService
import com.github.caay2000.ttk.context.entity.application.update.EntityUpdateStarterService
import com.github.caay2000.ttk.context.entity.application.update.EntityUpdateStopperService
import com.github.caay2000.ttk.context.entity.application.update.EntityUpdateUnloaderService
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException
import com.github.caay2000.ttk.context.location.application.LocationRepository
import com.github.caay2000.ttk.context.world.application.WorldRepository
import com.github.caay2000.ttk.shared.CompanyId

class EntityAllCompanyVehiclesUpdaterService(
    worldRepository: WorldRepository,
    locationRepository: LocationRepository,
    private val entityRepository: EntityRepository,
    eventPublisher: EventPublisher<Event>
) {

    private val loaderService = EntityUpdateLoaderService(locationRepository, eventPublisher)
    private val unloaderService = EntityUpdateUnloaderService(eventPublisher)
    private val moverService = EntityUpdateMoverService(worldRepository, eventPublisher)
    private val starterService = EntityUpdateStarterService(locationRepository, eventPublisher)
    private val stopperService = EntityUpdateStopperService(eventPublisher)

    fun invoke(companyId: CompanyId): Either<EntityException, Unit> =
        findAllEntities(companyId)
            .flatMap { entities -> entities.updateAll() }
            .void()

    private fun List<Entity>.updateAll(): Either<EntityException, Unit> =
        Either.catch { forEach { it.updateEntity() } }
            .mapLeft { UnknownEntityException(it) }

    private fun findAllEntities(companyId: CompanyId): Either<EntityException, List<Entity>> =
        entityRepository.findAll(ByCompanyId(companyId))
            .mapLeft { UnknownEntityException(it) }

    private fun Entity.updateEntity(): Entity =
        this.update().right()
            .flatMap { entity -> loaderService.invoke(entity) }
            .flatMap { entity -> starterService.invoke(entity) }
            .flatMap { entity -> moverService.invoke(entity) }
            .flatMap { entity -> stopperService.invoke(entity) }
            .flatMap { entity -> unloaderService.invoke(entity) }
            .flatMap { entity -> entity.save() }
            .bind()

    private fun Entity.save(): Either<EntityException, Entity> =
        entityRepository.save(this)
            .map { this }
            .mapLeft { UnknownEntityException(it) }
}
