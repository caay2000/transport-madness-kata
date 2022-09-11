package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import arrow.core.right
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException
import com.github.caay2000.ttk.context.location.application.LocationRepository
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.primary.query.FindLocationQuery
import com.github.caay2000.ttk.context.location.primary.query.FindLocationQueryResponse

class EntityUpdateStarterService(private val queryExecutor: QueryExecutor) {

    fun invoke(entity: Entity): Either<EntityException, Entity> =
        if (entity.isStopped) entity.resumeRoute()
        else entity.right()

    private fun Entity.resumeRoute(): Either<EntityException, Entity> =
        findLocation(this)
            .map { location -> shouldResumeRoute(location) }
            .map { shouldResume -> if (shouldResume) updateStart() else this }

    private fun findLocation(entity: Entity): Either<EntityException, Location> =
        Either.catch {
            queryExecutor.execute<FindLocationQueryResponse>(FindLocationQuery((LocationRepository.FindLocationCriteria.ByPositionCriteria(entity.currentPosition)))).value
        }.mapLeft { UnknownEntityException(it) }

    private fun Entity.shouldResumeRoute(location: Location): Boolean =
        currentDuration > location.configuration.cityDefaultWaitingTurnsForEntities
}
