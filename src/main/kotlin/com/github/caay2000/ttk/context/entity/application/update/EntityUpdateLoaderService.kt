package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityUpdateLoaderServiceEntityException
import com.github.caay2000.ttk.context.entity.domain.update.LoadPassengersStrategy

class EntityUpdateLoaderService(queryExecutor: QueryExecutor) {

    private val loadPassengersStrategy = LoadPassengersStrategy.SimpleLoadPassengersStrategy(queryExecutor)

    fun invoke(entity: Entity): Either<EntityException, Entity> =
        Either.catch { entity.updateLoad(loadPassengersStrategy) }
            .mapLeft { EntityUpdateLoaderServiceEntityException(it) }
}
