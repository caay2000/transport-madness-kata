package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.application.EntityRepository
import com.github.caay2000.ttk.context.entity.application.EntityService
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException
import com.github.caay2000.ttk.context.location.application.LocationRepository

class EntityUpdaterService(
    provider: Provider,
    entityRepository: EntityRepository,
    locationRepository: LocationRepository,
    eventPublisher: EventPublisher<Event>
) : EntityService(entityRepository, eventPublisher) {

    private val loaderService = EntityUpdateLoaderService(locationRepository, entityRepository, eventPublisher)
    private val unloaderService = EntityUpdateUnloaderService(entityRepository, eventPublisher)
    private val moverService = EntityUpdateMoverService(provider, entityRepository, eventPublisher)
    private val starterService = EntityUpdateStarterService(locationRepository, entityRepository, eventPublisher)
    private val stopperService = EntityUpdateStopperService(entityRepository, eventPublisher)

    fun invoke(): Either<EntityException, Unit> =
        findAllEntities()
            .tap { entities -> entities.forEach { it.updateEntity() } }
            .void()

    private fun findAllEntities(): Either<EntityException, Collection<Entity>> =
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
}
