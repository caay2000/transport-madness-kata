package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityUpdateStarterServiceEntityException
import com.github.caay2000.ttk.context.entity.domain.update.ShouldResumeRouteStrategy
import com.github.caay2000.ttk.context.entity.domain.update.ShouldResumeRouteStrategy.SimpleShouldResumeRouteStrategy

class EntityUpdateStarterService(queryExecutor: QueryExecutor) {

    private val shouldResumeRouteStrategy: ShouldResumeRouteStrategy = SimpleShouldResumeRouteStrategy(queryExecutor)

    fun invoke(initialEntity: Entity): Either<EntityException, Entity> =
        Either.catch { initialEntity.updateStart(shouldResumeRouteStrategy) }
            .mapLeft { EntityUpdateStarterServiceEntityException(it) }
}
