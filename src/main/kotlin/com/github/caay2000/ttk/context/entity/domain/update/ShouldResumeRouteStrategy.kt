package com.github.caay2000.ttk.context.entity.domain.update

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityStatus.IN_ROUTE
import com.github.caay2000.ttk.context.location.application.LocationRepository
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position

sealed class ShouldResumeRouteStrategy {

    abstract fun invoke(entity: Entity): Entity

    class SimpleShouldResumeRouteStrategy(private val locationRepository: LocationRepository) : ShouldResumeRouteStrategy() {

        override fun invoke(entity: Entity): Entity =
            findLocation(entity.currentPosition)
                .map { location -> entity.updateEntity(location) }
                .bind()

        private fun findLocation(currentPosition: Position) =
            locationRepository.find(LocationRepository.FindLocationCriteria.ByPosition(currentPosition))

        private fun Entity.updateEntity(location: Location) =
            if (shouldResumeRoute(location)) copy(route = route.nextStop(), status = IN_ROUTE, currentDuration = 0)
            else this

        private fun Entity.shouldResumeRoute(location: Location): Boolean =
            currentDuration > location.configuration.cityDefaultWaitingTurnsForEntities
    }
}
