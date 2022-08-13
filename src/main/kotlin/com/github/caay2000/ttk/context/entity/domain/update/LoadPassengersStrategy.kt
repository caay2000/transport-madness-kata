package com.github.caay2000.ttk.context.entity.domain.update

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.getOrHandle
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.event.EntityLoadedEvent
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World

sealed class LoadPassengersStrategy {

    abstract fun invoke(entity: Entity): Entity

    class SimpleLoadPassengersStrategy(private val provider: Provider) : LoadPassengersStrategy() {

        override fun invoke(entity: Entity): Entity =
            findWorld()
                .flatMap { world -> world.findLocation(entity.currentPosition) }
                .map { location -> entity.loadPassengers(location) }
                .getOrHandle { entity }

        private fun findWorld() = provider.get()

        private fun World.findLocation(currentPosition: Position) =
            Either.catch { this.getLocation(currentPosition) }

        private fun Entity.loadPassengers(location: Location): Entity =
            if (location.pax == 0) this
            else {
                this.copy(pax = pax + location.pax).also {
                    it.pushEvents(pullEvents() + EntityLoadedEvent(id, location.pax, currentPosition))
                }
            }
    }
}
