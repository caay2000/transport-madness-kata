package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityUpdateStarterServiceException
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException
import com.github.caay2000.ttk.context.entity.domain.update.ShouldResumeRouteStrategy
import com.github.caay2000.ttk.context.entity.domain.update.ShouldResumeRouteStrategy.SimpleShouldResumeRouteStrategy
import com.github.caay2000.ttk.context.location.application.LocationRepository

class EntityUpdateStarterService(
    locationRepository: LocationRepository,
    private val eventPublisher: EventPublisher<Event>
) {

    private val shouldResumeRouteStrategy: ShouldResumeRouteStrategy = SimpleShouldResumeRouteStrategy(locationRepository)

    fun invoke(initialEntity: Entity): Either<EntityException, Entity> =
        Either.catch { initialEntity.updateStart(shouldResumeRouteStrategy) }
            .mapLeft { EntityUpdateStarterServiceException(it) }
            .flatMap { entity -> entity.publishEvents() }

    private fun Entity.publishEvents(): Either<EntityException, Entity> =
        Either.catch { eventPublisher.publish(pullEvents()).let { this } }
            .mapLeft { UnknownEntityException(it) }
}
