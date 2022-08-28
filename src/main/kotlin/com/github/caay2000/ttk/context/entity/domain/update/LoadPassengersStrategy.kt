package com.github.caay2000.ttk.context.entity.domain.update

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.event.EntityLoadedEvent
import com.github.caay2000.ttk.context.location.application.LocationRepository.FindLocationCriteria.ByPositionCriteria
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.primary.query.LocationFinderQuery
import com.github.caay2000.ttk.context.location.primary.query.LocationFinderQueryResponse
import kotlin.math.min

sealed class LoadPassengersStrategy {

    abstract fun invoke(entity: Entity): Entity

    class SimpleLoadPassengersStrategy(private val queryExecutor: QueryExecutor) : LoadPassengersStrategy() {

        override fun invoke(entity: Entity): Entity =
            findLocation(entity)
                .let { location -> entity.checkAmountToLoad(location) }
                .let { amount -> entity.loadPassengers(amount) }

        private fun findLocation(entity: Entity): Location =
            queryExecutor.execute<LocationFinderQuery, LocationFinderQueryResponse>(LocationFinderQuery((ByPositionCriteria(entity.currentPosition)))).value

        private fun Entity.loadPassengers(amount: Int): Entity =
            Either.catch {
                if (amount == 0) this
                else {
                    this.copy(pax = pax + amount).also {
                        it.pushEvents(pullEvents() + EntityLoadedEvent(id, amount, currentPosition))
                    }
                }
            }.bind()

        private fun Entity.checkAmountToLoad(location: Location) =
            min(this.entityType.passengerCapacity - this.pax, location.pax)
    }
}
