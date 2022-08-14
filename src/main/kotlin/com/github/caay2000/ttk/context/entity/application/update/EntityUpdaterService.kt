package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.application.EntityException
import com.github.caay2000.ttk.context.entity.application.EntityService
import com.github.caay2000.ttk.context.entity.domain.Entity

class EntityUpdaterService(
    provider: Provider,
    eventPublisher: EventPublisher<Event>
) : EntityService(provider, eventPublisher) {

    private val loaderService = EntityUpdateLoaderService(provider, eventPublisher)
    private val unloaderService = EntityUpdateUnloaderService(provider, eventPublisher)
    private val moverService = EntityUpdateMoverService(provider, eventPublisher)
    private val starterService = EntityUpdateStarterService(provider, eventPublisher)
    private val stopperService = EntityUpdateStopperService(provider, eventPublisher)

    fun invoke(): Either<EntityException, Unit> =
        findAllEntities()
            .tap { entities -> entities.forEach { it.updateEntity() } }
            .void()

    private fun findAllEntities(): Either<EntityException, Collection<Entity>> =
        findWorld()
            .map { world -> world.entities.values }

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
