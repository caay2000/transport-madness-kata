package com.github.caay2000.ttk

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.configuration.application.ConfigurationSetterService
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.configuration.query.GetConfigurationQuery
import com.github.caay2000.ttk.context.configuration.query.GetConfigurationQueryHandler
import com.github.caay2000.ttk.context.entity.application.EntityCreatorService
import com.github.caay2000.ttk.context.entity.application.EntityRouteAssignerService
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.event.EntityLoadedEvent
import com.github.caay2000.ttk.context.entity.event.EntityUnloadedEvent
import com.github.caay2000.ttk.context.entity.query.EntityNextSectionQuery
import com.github.caay2000.ttk.context.entity.query.EntityNextSectionQueryHandler
import com.github.caay2000.ttk.context.location.event.UpdateLocationOnEntityLoadedEventSubscriber
import com.github.caay2000.ttk.context.location.event.UpdateLocationOnEntityUnloadedEventSubscriber
import com.github.caay2000.ttk.context.location.query.LocationPassengerAvailableQuery
import com.github.caay2000.ttk.context.location.query.LocationPassengerAvailableQueryHandler
import com.github.caay2000.ttk.context.world.application.WorldConnectionCreatorService
import com.github.caay2000.ttk.context.world.application.WorldCreatorService
import com.github.caay2000.ttk.context.world.application.WorldLocationCreatorService
import com.github.caay2000.ttk.context.world.application.WorldUpdaterService
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.infra.console.ConsolePrinter
import com.github.caay2000.ttk.infra.eventbus.KTEventBus
import com.github.caay2000.ttk.infra.eventbus.KTEventPublisher
import com.github.caay2000.ttk.infra.eventbus.KTQueryExecutor
import com.github.caay2000.ttk.infra.eventbus.instantiateEventSubscriber
import com.github.caay2000.ttk.infra.eventbus.instantiateQueryHandler
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.pathfinding.PathfindingConfiguration

class Application(
    private val configuration: Configuration,
    private val provider: Provider = DefaultProvider()
) {
    private val eventPublisher: EventPublisher<Event> = KTEventPublisher()
    private val queryExecutor: QueryExecutor = KTQueryExecutor()

    init {
        KTEventBus.init<Query, Event>()
        instantiateEventSubscriber(EntityUnloadedEvent::class, UpdateLocationOnEntityUnloadedEventSubscriber(provider, eventPublisher))
        instantiateEventSubscriber(EntityLoadedEvent::class, UpdateLocationOnEntityLoadedEventSubscriber(provider, eventPublisher))

        instantiateQueryHandler(EntityNextSectionQuery::class, EntityNextSectionQueryHandler(provider))
        instantiateQueryHandler(LocationPassengerAvailableQuery::class, LocationPassengerAvailableQueryHandler(provider))
        instantiateQueryHandler(GetConfigurationQuery::class, GetConfigurationQueryHandler(provider))
    }

    private val createConnectionPathfindingConfiguration = PathfindingConfiguration(needConnection = false)

    private val configurationSetterService = ConfigurationSetterService(provider)
    private val worldCreatorService = WorldCreatorService(provider, eventPublisher)
    private val worldUpdaterService = WorldUpdaterService(provider, eventPublisher, queryExecutor)
    private val worldConnectionCreatorService = WorldConnectionCreatorService(provider, eventPublisher, createConnectionPathfindingConfiguration)
    private val worldLocationCreatorService = WorldLocationCreatorService(provider, eventPublisher)
    private val entityCreatorService = EntityCreatorService(provider, eventPublisher)
    private val entityRouteAssignerService = EntityRouteAssignerService(provider, eventPublisher)
    private val printer = ConsolePrinter(configuration)

    fun invoke(startPosition: Position, paths: Map<Position, List<Position>>, locations: Set<Pair<Position, Int>>, route: List<Position>): Int {

        configurationSetterService.invoke(configuration).bind()
        worldCreatorService.invoke().bind()
        locations.forEach { (position, population) ->
            worldLocationCreatorService.invoke(position, population).bind()
        }
        createAllConnections(paths)
        entityCreatorService.invoke(startPosition).bind()
        entityRouteAssignerService.invoke(entity.id, route).bind()

        printer.print(world)
        while (checkRouteCompleted(startPosition).not()) {
            worldUpdaterService.invoke().bind()
            printer.print(world)
//            println("${world.currentTurn} - $entity")
            if (world.currentTurn > 100)
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

    private fun checkRouteCompleted(startPosition: Position): Boolean = world.currentTurn > 1 && entity.currentPosition == startPosition

    private val world: World
        get() = provider.get().bind()
    private val entity: Entity
        get() = provider.get().bind().entities.values.first()
}
