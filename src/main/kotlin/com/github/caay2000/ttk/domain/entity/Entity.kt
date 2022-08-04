package com.github.caay2000.ttk.domain.entity

import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.pathifinding.NextSectionPathfinding
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId

data class Entity(
    val id: EntityId,
    val currentPosition: Position,
    val currentDuration: Int,
    val route: Route,
    val status: EntityStatus = EntityStatus.STOP,
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

    private val destinationReached: Boolean
        get() = currentPosition == currentDestination && status == EntityStatus.IN_ROUTE

    private val shouldResumeRoute: Boolean
        get() = currentDuration > configuration.turnsStoppedInStation && status == EntityStatus.STOP

    private val shouldUpdateNextSection: Boolean
        get() = route.nextSectionList.isEmpty() && status == EntityStatus.IN_ROUTE

    private val shouldMove: Boolean
        get() = status == EntityStatus.IN_ROUTE

    fun assignRoute(route: Route) = copy(route = route)

    fun update(nextSectionFinder: NextSectionPathfinding): Entity =
        increaseDuration()
            .loadPassengers()
            .resumeRoute()
            .refreshNextSection(nextSectionFinder)
            .moveEntity()
            .stopEntity()

    private fun increaseDuration(): Entity = copy(currentDuration = currentDuration + 1)

    private fun loadPassengers(/*here we should add something similar to the nextSectionFinder*/): Entity =
        if (status == EntityStatus.STOP) this
        else this

    private fun resumeRoute(): Entity =
        if (shouldResumeRoute) copy(route = route.nextStop(), status = EntityStatus.IN_ROUTE, currentDuration = 0)
        else this

    private fun refreshNextSection(nextSectionFinder: NextSectionPathfinding): Entity =
        if (shouldUpdateNextSection) copy(route = route.updateNextSection(nextSectionFinder.invoke(currentPosition, currentDestination)))
        else this

    private fun moveEntity(): Entity =
        if (shouldMove) copy(currentPosition = route.nextSection.position, route = route.dropNextSection())
        else this

    private fun stopEntity(): Entity =
        if (destinationReached) copy(status = EntityStatus.STOP, currentDuration = 0)
        else this
}
