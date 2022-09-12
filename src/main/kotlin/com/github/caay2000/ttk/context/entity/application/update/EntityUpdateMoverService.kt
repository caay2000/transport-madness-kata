package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.UnknownEntityException
import com.github.caay2000.ttk.context.pathfinding.primary.query.FindNextSectionQuery
import com.github.caay2000.ttk.context.pathfinding.primary.query.FindNextSectionQueryResponse
import com.github.caay2000.ttk.context.world.application.WorldRepository
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World

class EntityUpdateMoverService(private val worldRepository: WorldRepository, private val queryExecutor: QueryExecutor) {

    fun invoke(entity: Entity): Either<EntityException, Entity> =
        if (entity.shouldMove) entity.move()
        else entity.right()

    private fun Entity.move(): Either<EntityException, Entity> =
        shouldUpdateNextSection()
            .flatMap { needNewSection -> if (needNewSection) this.updateNextSection() else this.right() }
            .map { entity -> entity.updateMove() }

    private fun Entity.shouldUpdateNextSection(): Either<EntityException, Boolean> = route.nextSectionList.isEmpty().right()

    // TODO this findWorld should be moved to pathfinding context
    private fun Entity.updateNextSection(): Either<EntityException, Entity> =
        findWorld()
            .map { world -> world.findNextSection(currentPosition, route.currentDestination) }
            .map { section -> updateNextSection(section.value) }
            .mapLeft { UnknownEntityException(it) }

    private fun findWorld() = worldRepository.get()

    private fun World.findNextSection(source: Position, target: Position) =
        queryExecutor.execute<FindNextSectionQueryResponse>(
            FindNextSectionQuery(
                cells = cells.values.filter { it.connected },
                source = getCell(source),
                target = getCell(target)
            )
        )
}
