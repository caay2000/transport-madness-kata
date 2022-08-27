package com.github.caay2000.ttk

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.CommandBus
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.company.application.CompanyCreatorService
import com.github.caay2000.ttk.context.company.application.CompanyRepository
import com.github.caay2000.ttk.context.company.primary.command.UpdateAllCompaniesCommand
import com.github.caay2000.ttk.context.company.primary.command.UpdateAllCompaniesCommandHandler
import com.github.caay2000.ttk.context.company.primary.event.AddEntityToCompanyOnEntityCreatedEventSubscriber
import com.github.caay2000.ttk.context.company.secondary.InMemoryCompanyRepository
import com.github.caay2000.ttk.context.configuration.application.ConfigurationSetterService
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.entity.application.EntityCreatorService
import com.github.caay2000.ttk.context.entity.application.EntityRepository
import com.github.caay2000.ttk.context.entity.application.EntityRouteAssignerService
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityType
import com.github.caay2000.ttk.context.entity.event.EntityCreatedEvent
import com.github.caay2000.ttk.context.entity.event.EntityLoadedEvent
import com.github.caay2000.ttk.context.entity.event.EntityUnloadedEvent
import com.github.caay2000.ttk.context.entity.primary.command.UpdateAllCompanyVehiclesCommand
import com.github.caay2000.ttk.context.entity.primary.command.UpdateAllCompanyVehiclesCommandHandler
import com.github.caay2000.ttk.context.entity.secondary.InMemoryEntityRepository
import com.github.caay2000.ttk.context.location.application.LocationCreatorService
import com.github.caay2000.ttk.context.location.application.LocationRepository
import com.github.caay2000.ttk.context.location.event.LocationCreatedEvent
import com.github.caay2000.ttk.context.location.primary.command.UpdateAllLocationsCommand
import com.github.caay2000.ttk.context.location.primary.command.UpdateAllLocationsCommandHandler
import com.github.caay2000.ttk.context.location.primary.event.UpdateLocationOnEntityLoadedEventSubscriber
import com.github.caay2000.ttk.context.location.primary.event.UpdateLocationOnEntityUnloadedEventSubscriber
import com.github.caay2000.ttk.context.location.secondary.InMemoryLocationRepository
import com.github.caay2000.ttk.context.world.application.WorldConnectionCreatorService
import com.github.caay2000.ttk.context.world.application.WorldCreatorService
import com.github.caay2000.ttk.context.world.application.WorldRepository
import com.github.caay2000.ttk.context.world.application.WorldUpdaterService
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.primary.AddLocationToWorldOnLocationCreatedEventSubscriber
import com.github.caay2000.ttk.context.world.secondary.InMemoryWorldRepository
import com.github.caay2000.ttk.infra.console.HexagonalConsolePrinter
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import com.github.caay2000.ttk.infra.eventbus.KTCommandBus
import com.github.caay2000.ttk.infra.eventbus.KTEventBus
import com.github.caay2000.ttk.infra.eventbus.KTEventPublisher
import com.github.caay2000.ttk.infra.eventbus.instantiateCommandHandler
import com.github.caay2000.ttk.infra.eventbus.instantiateEventSubscriber

class Application(
    private val configuration: Configuration,
    inMemoryDatabase: InMemoryDatabase
) {
    private val eventPublisher: EventPublisher<Event> = KTEventPublisher()
    private val commandBus: CommandBus<Command> = KTCommandBus()
    private val locationRepository: LocationRepository = InMemoryLocationRepository(inMemoryDatabase)
    private val companyRepository: CompanyRepository = InMemoryCompanyRepository(inMemoryDatabase)
    private val entityRepository: EntityRepository = InMemoryEntityRepository(inMemoryDatabase)
    private val worldRepository: WorldRepository = InMemoryWorldRepository(inMemoryDatabase)

    private val addLocationToWorldOnLocationCreatedEventSubscriber = AddLocationToWorldOnLocationCreatedEventSubscriber(worldRepository, eventPublisher)
    private val addEntityToCompanyOnEntityCreatedEventSubscriber = AddEntityToCompanyOnEntityCreatedEventSubscriber(companyRepository, eventPublisher)
    private val updateLocationOnEntityUnloadedEventSubscriber = UpdateLocationOnEntityUnloadedEventSubscriber(locationRepository, eventPublisher)
    private val updateLocationOnEntityLoadedEventSubscriber = UpdateLocationOnEntityLoadedEventSubscriber(locationRepository, eventPublisher)
    private val updateAllLocationsCommandHandler = UpdateAllLocationsCommandHandler(locationRepository, eventPublisher)
    private val updateAllCompaniesCommandHandler = UpdateAllCompaniesCommandHandler(companyRepository, commandBus, eventPublisher)
    private val updateAllCompanyVehiclesCommandHandler = UpdateAllCompanyVehiclesCommandHandler(worldRepository, locationRepository, entityRepository, eventPublisher)

    init {
        KTEventBus.init<Command, Event>()
        instantiateEventSubscriber(LocationCreatedEvent::class, addLocationToWorldOnLocationCreatedEventSubscriber)
        instantiateEventSubscriber(EntityCreatedEvent::class, addEntityToCompanyOnEntityCreatedEventSubscriber)
        instantiateEventSubscriber(EntityUnloadedEvent::class, updateLocationOnEntityUnloadedEventSubscriber)
        instantiateEventSubscriber(EntityLoadedEvent::class, updateLocationOnEntityLoadedEventSubscriber)
        instantiateCommandHandler(UpdateAllLocationsCommand::class, updateAllLocationsCommandHandler)
        instantiateCommandHandler(UpdateAllCompaniesCommand::class, updateAllCompaniesCommandHandler)
        instantiateCommandHandler(UpdateAllCompanyVehiclesCommand::class, updateAllCompanyVehiclesCommandHandler)
    }

    private val configurationSetterService = ConfigurationSetterService()
    private val companyCreatorService = CompanyCreatorService(companyRepository, eventPublisher)
    private val worldCreatorService = WorldCreatorService(worldRepository, eventPublisher)
    private val worldUpdaterService = WorldUpdaterService(worldRepository, commandBus, eventPublisher)
    private val worldConnectionCreatorService = WorldConnectionCreatorService(worldRepository, eventPublisher)
    private val locationCreatorService = LocationCreatorService(locationRepository, eventPublisher)
    private val entityCreatorService = EntityCreatorService(worldRepository, companyRepository, entityRepository, eventPublisher)
    private val entityRouteAssignerService = EntityRouteAssignerService(entityRepository, eventPublisher)
    private val printer = HexagonalConsolePrinter(locationRepository, entityRepository, configuration)

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
        val company = companyCreatorService.invoke("Company").bind()
        worldCreatorService.invoke().bind()
        locations.forEach { (name, position, population) ->
            locationCreatorService.invoke(name, position, population).bind()
        }
        createAllConnections(paths)
        startPosition.forEach {
            entityCreatorService.invoke(company.id, entityType, it).bind()
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
        get() = worldRepository.get().bind()
    private val entity: Entity
        get() = entityRepository.findAll().bind().first()

    data class LocationRequest(val name: String, val position: Position, val population: Int)
}
