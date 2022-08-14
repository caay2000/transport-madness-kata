package com.github.caay2000.ttk.context.entity.domain.update

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.event.EntityLoadedEvent
import com.github.caay2000.ttk.context.location.domain.Location
import kotlin.math.min

sealed class LoadPassengersStrategy {

    abstract fun invoke(entity: Entity): Entity

    class SimpleLoadPassengersStrategy(private val provider: Provider) : LoadPassengersStrategy() {

        override fun invoke(entity: Entity): Entity =
            provider.getLocation(entity.currentPosition)
                .map { location -> entity.checkAmountToLoad(location) }
                .map { amount -> entity.loadPassengers(amount) }
                .bind()

        private fun Entity.loadPassengers(amount: Int): Entity =
            if (amount == 0) this
            else {
                this.copy(pax = pax + amount).also {
                    it.pushEvents(pullEvents() + EntityLoadedEvent(id, amount, currentPosition))
                }
            }

        private fun Entity.checkAmountToLoad(location: Location) =
            min(this.entityType.passengerCapacity - this.pax, location.pax)
    }
}
