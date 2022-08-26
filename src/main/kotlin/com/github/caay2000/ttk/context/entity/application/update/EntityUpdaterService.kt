package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.application.EntityRepository
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException
import com.github.caay2000.ttk.context.location.application.LocationRepository
import com.github.caay2000.ttk.context.world.application.WorldRepository

class EntityUpdaterService(
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

    fun invoke(): Either<EntityException, Unit> =
        findAllEntities()
            .flatMap { entities -> entities.updateAll() }
            .void()

    private fun List<Entity>.updateAll(): Either<EntityException, Unit> =
        Either.catch { forEach { it.updateEntity() } }
            .mapLeft { UnknownEntityException(it) }

    private fun findAllEntities(): Either<EntityException, List<Entity>> =
        entityRepository.findAll()
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
