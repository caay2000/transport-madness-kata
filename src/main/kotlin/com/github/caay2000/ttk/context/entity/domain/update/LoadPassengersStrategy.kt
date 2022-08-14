package com.github.caay2000.ttk.context.entity.domain.update

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.event.EntityLoadedEvent
import com.github.caay2000.ttk.context.location.domain.Location

sealed class LoadPassengersStrategy {

    abstract fun invoke(entity: Entity): Entity

    class SimpleLoadPassengersStrategy(private val provider: Provider) : LoadPassengersStrategy() {

        override fun invoke(entity: Entity): Entity =
            provider.getLocation(entity.currentPosition)
                .map { location -> entity.loadPassengers(location) }
                .bind()

        private fun Entity.loadPassengers(location: Location): Entity =
            if (location.pax == 0) this
            else {
                this.copy(pax = pax + location.pax).also {
                    it.pushEvents(pullEvents() + EntityLoadedEvent(id, location.pax, currentPosition))
                }
            }
    }
}
