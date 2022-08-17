package com.github.caay2000.ttk

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.configuration.application.ConfigurationSetterService
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.entity.application.EntityCreatorService
import com.github.caay2000.ttk.context.entity.application.EntityRouteAssignerService
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityType
import com.github.caay2000.ttk.context.entity.event.EntityLoadedEvent
import com.github.caay2000.ttk.context.entity.event.EntityUnloadedEvent
import com.github.caay2000.ttk.context.location.application.LocationCreatorService
import com.github.caay2000.ttk.context.location.event.UpdateLocationOnEntityLoadedEventSubscriber
import com.github.caay2000.ttk.context.location.event.UpdateLocationOnEntityUnloadedEventSubscriber
import com.github.caay2000.ttk.context.world.application.WorldConnectionCreatorService
import com.github.caay2000.ttk.context.world.application.WorldCreatorService
import com.github.caay2000.ttk.context.world.application.WorldUpdaterService
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.infra.console.HexagonalConsolePrinter
import com.github.caay2000.ttk.infra.eventbus.KTEventBus
import com.github.caay2000.ttk.infra.eventbus.KTEventPublisher
import com.github.caay2000.ttk.infra.eventbus.instantiateEventSubscriber
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.pathfinding.PathfindingConfiguration

class Application(
    private val configuration: Configuration,
    private val provider: Provider = DefaultProvider()
) {
    private val eventPublisher: EventPublisher<Event> = KTEventPublisher()

    init {
        KTEventBus.init<Event>()
        instantiateEventSubscriber(EntityUnloadedEvent::class, UpdateLocationOnEntityUnloadedEventSubscriber(provider, eventPublisher))
        instantiateEventSubscriber(EntityLoadedEvent::class, UpdateLocationOnEntityLoadedEventSubscriber(provider, eventPublisher))
    }

    private val createConnectionPathfindingConfiguration = PathfindingConfiguration(needConnection = false)

    private val configurationSetterService = ConfigurationSetterService(provider)
    private val worldCreatorService = WorldCreatorService(provider, eventPublisher)
    private val worldUpdaterService = WorldUpdaterService(provider, eventPublisher)
    private val worldConnectionCreatorService = WorldConnectionCreatorService(provider, eventPublisher, createConnectionPathfindingConfiguration)
    private val locationCreatorService = LocationCreatorService(provider, eventPublisher)
    private val entityCreatorService = EntityCreatorService(provider, eventPublisher)
    private val entityRouteAssignerService = EntityRouteAssignerService(provider, eventPublisher)
    private val printer = HexagonalConsolePrinter(provider, configuration)

    fun invoke(
        entityType: EntityType,
        startPosition: List<Position>,
        paths: Map<Position, List<Position>>,
        locations: Set<LocationRequest>,
        route: List<Position>,
        timesToComplete: Int = 1
    ): Int {

        var completed = 0

        configurationSetterService.invoke(configuration).bind()
        worldCreatorService.invoke().bind()
        locations.forEach { (name, position, population) ->
            locationCreatorService.invoke(name, position, population).bind()
        }
        createAllConnections(paths)
        startPosition.forEach {
            entityCreatorService.invoke(entityType, it).bind()
        }
        entityRouteAssignerService.invoke(entity.id, route).bind()

        printer.print(world)

        while (timesToComplete > completed) {
            worldUpdaterService.invoke().bind()
            printer.print(world)

            if (checkRouteCompleted(startPosition.first())) completed++
            if (world.currentTurn > 10000)
                return -1
        }
        return world.currentTurn
    }

    private fun createAllConnections(paths: Map<Position, List<Position>>) {
        paths.forEach { (source, targetList) ->
            targetList.forEach { target ->
                worldConnectionCreatorService.invoke(source, target).bind()
            }
        }
    }

    private fun checkRouteCompleted(startPosition: Position): Boolean =
        world.currentTurn > 1 &&
            entity.currentPosition == startPosition &&
            entity.currentDuration == 0 &&
            entity.route.stopIndex == 0

    private val world: World
        get() = provider.get().bind()
    private val entity: Entity
        get() = provider.get().bind().entities.values.first()

    data class LocationRequest(val name: String, val position: Position, val population: Int)
}
