package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.context.company.domain.Company
import com.github.caay2000.ttk.context.company.primary.query.FindCompanyQuery
import com.github.caay2000.ttk.context.company.primary.query.FindCompanyQueryResponse
import com.github.caay2000.ttk.context.pathfinding.primary.query.FindPathQuery
import com.github.caay2000.ttk.context.pathfinding.primary.query.FindPathQueryResponse
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.CompanyNotFoundException
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.UnknownWorldException
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.WorldException
import com.github.caay2000.ttk.shared.CompanyId

class WorldConnectionCreatorService(worldRepository: WorldRepository, private val queryExecutor: QueryExecutor, eventPublisher: EventPublisher) {

    private val worldService: WorldServiceApi = worldService(worldRepository, eventPublisher)

    fun invoke(companyId: CompanyId, source: Position, target: Position): Either<WorldException, World> =
        guardCompanyExists(companyId)
            .flatMap { worldService.find() }
            .flatMap { world -> world.createConnection(source, target) }
            .flatMap { world -> worldService.save(world) }
            .flatMap { world -> worldService.publishEvents(world) }

    private fun guardCompanyExists(companyId: CompanyId): Either<WorldException, Company> =
        Either.catch { queryExecutor.execute<FindCompanyQueryResponse>(FindCompanyQuery(companyId)).value }
            .mapLeft { CompanyNotFoundException(it) }

    private fun World.createConnection(source: Position, target: Position): Either<WorldException, World> =
        findConnection(source, target)
            .map { path -> path.updateCellsConnection() }
            .map { updatedCells -> createConnection(updatedCells) }
            .mapLeft { error -> UnknownWorldException(error) }

    private fun List<Cell>.updateCellsConnection(): List<Cell> =
        this.map { cell -> cell.createConnection() }

    private fun World.findConnection(source: Position, target: Position) =
        Either.catch {
            queryExecutor.execute<FindPathQueryResponse>(
                FindPathQuery(
                    needConnection = false,
                    cells = cells.values,
                    source = getCell(source),
                    target = getCell(target)
                )
            ).value
        }.mapLeft { UnknownWorldException(it) }
}
