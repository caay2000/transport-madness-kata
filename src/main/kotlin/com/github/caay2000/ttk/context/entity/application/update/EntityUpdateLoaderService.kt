package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.application.EntityException
import com.github.caay2000.ttk.context.entity.application.EntityService
import com.github.caay2000.ttk.context.entity.application.EntityUpdateLoaderServiceException
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.update.LoadPassengersStrategy

class EntityUpdateLoaderService(provider: Provider, eventPublisher: EventPublisher<Event>) : EntityService(provider, eventPublisher) {

    private val loadPassengersStrategy = LoadPassengersStrategy.SimpleLoadPassengersStrategy(provider)

    fun invoke(initialEntity: Entity): Either<EntityException, Entity> =
        Either.catch { initialEntity.updateLoad(loadPassengersStrategy) }
            .mapLeft { EntityUpdateLoaderServiceException(it) }
            .flatMap { entity -> entity.save() }
            .flatMap { entity -> entity.publishEvents() }
}
