package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import arrow.core.right
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException
import com.github.caay2000.ttk.context.location.application.LocationRepository.FindLocationCriteria.ByPositionCriteria
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.primary.query.FindLocationQuery
import com.github.caay2000.ttk.context.location.primary.query.FindLocationQueryResponse
import kotlin.math.min

class EntityUpdateLoaderService(private val queryExecutor: QueryExecutor) {

    fun invoke(entity: Entity): Either<EntityException, Entity> =
        if (entity.isStopped && entity.currentDuration == 1) entity.updateLoad()
        else entity.right()

    private fun Entity.updateLoad(): Either<EntityException, Entity> =
        findLocation(this)
            .map { location -> this.checkAmountToLoad(location) }
            .map { amount -> this.loadPassengers(amount) }
            .mapLeft { UnknownEntityException(it) }

    private fun findLocation(entity: Entity): Either<EntityException, Location> =
        Either.catch {
            queryExecutor.execute<FindLocationQueryResponse>(FindLocationQuery((ByPositionCriteria(entity.currentPosition)))).value
        }.mapLeft { UnknownEntityException(it) }

    private fun Entity.checkAmountToLoad(location: Location) =
        min(this.entityType.passengerCapacity - this.pax, location.pax)

    private fun Entity.loadPassengers(amount: Int): Entity =
        if (amount > 0) this.updateLoad(amount) else this
}
