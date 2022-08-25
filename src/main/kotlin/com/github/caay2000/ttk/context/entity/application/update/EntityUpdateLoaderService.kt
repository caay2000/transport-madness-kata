package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.application.EntityRepository
import com.github.caay2000.ttk.context.entity.application.EntityService
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityUpdateLoaderServiceException
import com.github.caay2000.ttk.context.entity.domain.update.LoadPassengersStrategy
import com.github.caay2000.ttk.context.location.application.LocationRepository

class EntityUpdateLoaderService(
    locationRepository: LocationRepository,
    entityRepository: EntityRepository,
    eventPublisher: EventPublisher<Event>
) : EntityService(entityRepository, eventPublisher) {

    private val loadPassengersStrategy = LoadPassengersStrategy.SimpleLoadPassengersStrategy(locationRepository)

    fun invoke(initialEntity: Entity): Either<EntityException, Entity> =
        Either.catch { initialEntity.updateLoad(loadPassengersStrategy) }
            .mapLeft { EntityUpdateLoaderServiceException(it) }
            .flatMap { entity -> entity.publishEvents() }
}
