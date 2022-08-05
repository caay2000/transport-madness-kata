package com.github.caay2000.ttk.application

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.application.configuration.ConfigurationSetterService
import com.github.caay2000.ttk.application.entity.EntityCreatorService
import com.github.caay2000.ttk.application.entity.EntityRouteAssignerService
import com.github.caay2000.ttk.application.pathfinding.PathfindingConfiguration
import com.github.caay2000.ttk.application.world.create.WorldConnectionCreatorService
import com.github.caay2000.ttk.application.world.create.WorldCreatorService
import com.github.caay2000.ttk.application.world.create.WorldLocationCreatorService
import com.github.caay2000.ttk.application.world.update.WorldUpdaterService
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.infra.console.ConsolePrinter
import com.github.caay2000.ttk.infra.eventbus.event.Event
import com.github.caay2000.ttk.infra.eventbus.event.EventBus
import com.github.caay2000.ttk.infra.eventbus.event.EventPublisher
import com.github.caay2000.ttk.infra.eventbus.event.EventPublisherImpl
import com.github.caay2000.ttk.infra.eventbus.impl.KTEventBus
import com.github.caay2000.ttk.infra.provider.DefaultProvider

class Application(
    private val configuration: Configuration,
    private val provider: Provider = DefaultProvider()
) {

    private val eventBus: EventBus<Event> = KTEventBus.init()
    private val eventPublisher: EventPublisher<Event> = EventPublisherImpl()

    private val createConnectionPathfindingConfiguration = PathfindingConfiguration(needConnection = false)

    private val configurationSetterService = ConfigurationSetterService(provider)
    private val worldCreatorService = WorldCreatorService(provider, eventPublisher)
    private val worldUpdaterService = WorldUpdaterService(provider, eventPublisher)
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
