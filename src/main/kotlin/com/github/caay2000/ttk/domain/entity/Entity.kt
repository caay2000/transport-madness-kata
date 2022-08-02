package com.github.caay2000.ttk.domain.entity

import com.github.caay2000.ttk.application.pathfinding.PathfindingResult
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId

data class Entity(
    val id: EntityId,
    val currentPosition: Position,
    val currentDuration: Int,
    val route: Route,
    val status: EntityStatus = EntityStatus.STOP,
    @Transient
    val configuration: Configuration
) {

    companion object {
        fun create(id: EntityId = randomDomainId(), position: Position, configuration: Configuration): Entity = Entity(
            id = id,
            currentPosition = position,
            currentDuration = 0,
            route = Route.create(listOf(position)),
            configuration = configuration
        )
    }

    private val currentDestination: Position
        get() = route.currentDestination

    private val shouldResumeRoute: Boolean
        get() = currentDuration >= configuration.turnsStoppedInStation && status == EntityStatus.STOP

    private val shouldUpdateNextSection: Boolean
        get() = shouldResumeRoute || (route.nextSection.isEmpty() && status == EntityStatus.IN_ROUTE)

    fun assignRoute(route: Route) = copy(route = route)

    fun update(nextSectionFinder: (Position, Position) -> PathfindingResult): Entity =
        when (status) {
            EntityStatus.STOP -> updateInStop(nextSectionFinder)
            EntityStatus.IN_ROUTE -> updateInRoute(nextSectionFinder)
        }

    private fun updateInStop(nextSectionFinder: (Position, Position) -> PathfindingResult): Entity = when {
        shouldResumeRoute -> copy(route = route.nextStop(), status = EntityStatus.IN_ROUTE, currentDuration = 0).updateInRoute(nextSectionFinder)
        else -> copy(currentDuration = currentDuration + 1)
    }

    private fun updateInRoute(nextSectionFinder: (Position, Position) -> PathfindingResult): Entity =
        updateNextSection(nextSectionFinder)
            .let {
                val (nextPosition, route) = it.route.popNextSection()
                val stopReached = nextPosition.position == it.route.currentDestination
                return if (stopReached) {
                    it.copy(currentPosition = nextPosition.position, status = EntityStatus.STOP, currentDuration = 0, route = route)
                } else it.copy(currentPosition = nextPosition.position, currentDuration = currentDuration + 1, route = route)
            }

    private fun updateNextSection(nextSectionFinder: (Position, Position) -> PathfindingResult): Entity =
        if (shouldResumeRoute || shouldUpdateNextSection)
            copy(route = route.copy(nextSection = nextSectionFinder.invoke(currentPosition, currentDestination).path))
        else this
}
