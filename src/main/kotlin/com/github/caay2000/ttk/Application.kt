package com.github.caay2000.ttk

import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.CommandBus
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.context.company.application.CompanyCreatorService
import com.github.caay2000.ttk.context.company.application.CompanyRepository
import com.github.caay2000.ttk.context.company.primary.command.UpdateAllCompaniesCommand
import com.github.caay2000.ttk.context.company.primary.command.UpdateAllCompaniesCommandHandler
import com.github.caay2000.ttk.context.company.primary.event.AddEntityToCompanyOnEntityCreatedEventSubscriber
import com.github.caay2000.ttk.context.company.primary.query.FindCompanyQuery
import com.github.caay2000.ttk.context.company.primary.query.FindCompanyQueryHandler
import com.github.caay2000.ttk.context.company.secondary.InMemoryCompanyRepository
import com.github.caay2000.ttk.context.configuration.application.ConfigurationSetterService
import com.github.caay2000.ttk.context.entity.application.EntityCreatorService
import com.github.caay2000.ttk.context.entity.application.EntityRepository
import com.github.caay2000.ttk.context.entity.application.EntityRouteAssignerService
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
import com.github.caay2000.ttk.context.location.primary.query.FindLocationQuery
import com.github.caay2000.ttk.context.location.primary.query.FindLocationQueryHandler
import com.github.caay2000.ttk.context.location.secondary.InMemoryLocationRepository
import com.github.caay2000.ttk.context.pathfinding.primary.query.FindPathQuery
import com.github.caay2000.ttk.context.pathfinding.primary.query.FindPathQueryHandler
import com.github.caay2000.ttk.context.world.application.WorldConnectionCreatorService
import com.github.caay2000.ttk.context.world.application.WorldCreatorService
import com.github.caay2000.ttk.context.world.application.WorldRepository
import com.github.caay2000.ttk.context.world.application.WorldUpdaterService
import com.github.caay2000.ttk.context.world.primary.event.AddLocationToWorldOnLocationCreatedEventSubscriber
import com.github.caay2000.ttk.context.world.primary.query.FindWorldQuery
import com.github.caay2000.ttk.context.world.primary.query.FindWorldQueryHandler
import com.github.caay2000.ttk.context.world.secondary.InMemoryWorldRepository
import com.github.caay2000.ttk.infra.console.HexagonalConsolePrinter
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import com.github.caay2000.ttk.infra.eventbus.KTCommandBus
import com.github.caay2000.ttk.infra.eventbus.KTEventBus
import com.github.caay2000.ttk.infra.eventbus.KTEventPublisher
import com.github.caay2000.ttk.infra.eventbus.KTQueryExecutor
import com.github.caay2000.ttk.infra.eventbus.instantiateCommandHandler
import com.github.caay2000.ttk.infra.eventbus.instantiateEventSubscriber
import com.github.caay2000.ttk.infra.eventbus.instantiateQueryHandler

internal class Application(inMemoryDatabase: InMemoryDatabase) {

    private val commandBus: CommandBus = KTCommandBus()
    private val queryExecutor: QueryExecutor = KTQueryExecutor()
    private val eventPublisher: EventPublisher = KTEventPublisher()

    private val locationRepository: LocationRepository = InMemoryLocationRepository(inMemoryDatabase)
    private val companyRepository: CompanyRepository = InMemoryCompanyRepository(inMemoryDatabase)
    private val entityRepository: EntityRepository = InMemoryEntityRepository(inMemoryDatabase)
    private val worldRepository: WorldRepository = InMemoryWorldRepository(inMemoryDatabase)

    init {
        KTEventBus.init<Command, Query, Event>()
        instantiateEventSubscriber(LocationCreatedEvent::class, AddLocationToWorldOnLocationCreatedEventSubscriber(worldRepository, eventPublisher))
        instantiateEventSubscriber(EntityCreatedEvent::class, AddEntityToCompanyOnEntityCreatedEventSubscriber(companyRepository, eventPublisher))
        instantiateEventSubscriber(EntityUnloadedEvent::class, UpdateLocationOnEntityUnloadedEventSubscriber(locationRepository, eventPublisher))
        instantiateEventSubscriber(EntityLoadedEvent::class, UpdateLocationOnEntityLoadedEventSubscriber(locationRepository, eventPublisher))

        instantiateQueryHandler(FindPathQuery::class, FindPathQueryHandler())
        instantiateQueryHandler(FindLocationQuery::class, FindLocationQueryHandler(locationRepository))
        instantiateQueryHandler(FindWorldQuery::class, FindWorldQueryHandler(worldRepository))
        instantiateQueryHandler(FindCompanyQuery::class, FindCompanyQueryHandler(companyRepository))

        instantiateCommandHandler(UpdateAllLocationsCommand::class, UpdateAllLocationsCommandHandler(locationRepository, eventPublisher))
        instantiateCommandHandler(UpdateAllCompaniesCommand::class, UpdateAllCompaniesCommandHandler(companyRepository, commandBus, eventPublisher))
        instantiateCommandHandler(UpdateAllCompanyVehiclesCommand::class, UpdateAllCompanyVehiclesCommandHandler(entityRepository, queryExecutor, eventPublisher))
    }

    internal val configurationSetterService = ConfigurationSetterService()
    internal val companyCreatorService = CompanyCreatorService(companyRepository, eventPublisher)
    internal val worldCreatorService = WorldCreatorService(worldRepository, eventPublisher)
    internal val worldUpdaterService = WorldUpdaterService(worldRepository, commandBus, eventPublisher)
    internal val worldConnectionCreatorService = WorldConnectionCreatorService(worldRepository, queryExecutor, eventPublisher)
    internal val locationCreatorService = LocationCreatorService(locationRepository, eventPublisher)
    internal val entityCreatorService = EntityCreatorService(worldRepository, companyRepository, entityRepository, eventPublisher)
    internal val entityRouteAssignerService = EntityRouteAssignerService(entityRepository, eventPublisher)
    internal val printer = HexagonalConsolePrinter(worldRepository, locationRepository, entityRepository)
}
