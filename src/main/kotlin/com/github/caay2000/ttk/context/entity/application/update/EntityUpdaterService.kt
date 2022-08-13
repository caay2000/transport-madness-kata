package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.application.EntityException
import com.github.caay2000.ttk.context.entity.application.EntityService
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.shared.EntityId

class EntityUpdaterService(
    provider: Provider,
    eventPublisher: EventPublisher<Event>
) : EntityService(provider, eventPublisher) {

    private val loaderService = EntityUpdateLoaderService(provider, eventPublisher)
    private val unloaderService = EntityUpdateUnloaderService(provider, eventPublisher)
    private val moverService = EntityUpdateMoverService(provider, eventPublisher)
    private val starterService = EntityUpdateStarterService(provider, eventPublisher)
    private val stopperService = EntityUpdateStopperService(provider, eventPublisher)

    fun invoke(entityId: EntityId): Either<EntityException, Entity> =
        findEntity(entityId)
            .flatMap { entity -> entity.updateEntity() }
            .flatMap { entity -> entity.save() }
            .flatMap { entity -> entity.publishEvents() }

    private fun Entity.updateEntity(): Either<EntityException, Entity> =
        update().right()
            .flatMap { entity -> loaderService.invoke(entity) }
            .flatMap { entity -> starterService.invoke(entity) }
            .flatMap { entity -> moverService.invoke(entity) }
            .flatMap { entity -> stopperService.invoke(entity) }
            .flatMap { entity -> unloaderService.invoke(entity) }
}
