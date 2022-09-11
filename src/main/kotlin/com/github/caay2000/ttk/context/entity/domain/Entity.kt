package com.github.caay2000.ttk.context.entity.domain

import com.github.caay2000.ttk.context.entity.domain.update.EntityMovementStrategy
import com.github.caay2000.ttk.context.entity.event.EntityCreatedEvent
import com.github.caay2000.ttk.context.entity.event.EntityLoadedEvent
import com.github.caay2000.ttk.context.entity.event.EntityUnloadedEvent
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.shared.Aggregate
import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.randomDomainId

data class Entity(
    override val id: EntityId,
    val companyId: CompanyId,
    val entityType: EntityType,
    val currentPosition: Position,
    val currentDuration: Int,
    val route: Route,
    val status: EntityStatus = EntityStatus.STOP,
    val pax: Int
) : Aggregate() {

    companion object {
        fun create(companyId: CompanyId, entityType: EntityType, position: Position): Entity = Entity(
            id = randomDomainId(),
            companyId = companyId,
            entityType = entityType,
            currentPosition = position,
            currentDuration = 0,
            route = Route.create(listOf(position)),
            pax = 0
        ).also {
            it.pushEvent(EntityCreatedEvent(aggregateId = it.id, companyId = it.companyId, entityType = it.entityType))
        }
    }

    private val currentDestination: Position
        get() = route.currentDestination

    private val destinationReached: Boolean
        get() = isInRoute && currentPosition == currentDestination

    private val shouldMove: Boolean
        get() = isInRoute && isRouteAssigned

    private val isRouteAssigned: Boolean
        get() = route.stops.size > 1

    val isInRoute: Boolean
        get() = status == EntityStatus.IN_ROUTE

    val isStopped: Boolean
        get() = status == EntityStatus.STOP

    fun assignRoute(route: Route) = copy(route = route)

    fun update(): Entity = copy(currentDuration = currentDuration + 1)

    fun updateLoad(amount: Int): Entity = copy(pax = pax + amount)
        .also { it.pushEvents(pullEvents() + EntityLoadedEvent(id, amount, currentPosition)) }

    fun updateStart(): Entity = copy(route = route.nextStop(), status = EntityStatus.IN_ROUTE, currentDuration = 0)

    fun updateMove(entityMovementStrategy: EntityMovementStrategy): Entity =
        if (shouldMove) entityMovementStrategy.invoke(this)
        else this

    fun updateStop(): Entity =
        if (destinationReached) copy(status = EntityStatus.STOP, currentDuration = 0)
        else this

    fun updateUnload(): Entity =
        if (isStopped && currentDuration == 0 && pax > 0) {
            copy(pax = 0).also { it.pushEvents(pullEvents() + EntityUnloadedEvent(id, pax, currentPosition)) }
        } else this
}
