package com.github.caay2000.ttk.application.entity.update

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.application.entity.EntityException
import com.github.caay2000.ttk.application.entity.EntityService
import com.github.caay2000.ttk.application.entity.UnknownEntityException
import com.github.caay2000.ttk.application.pathfinding.AStartPathfindingStrategy
import com.github.caay2000.ttk.application.pathfinding.PathfindingConfiguration
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.shared.EntityId

class EntityUpdaterService(provider: Provider) : EntityService(provider) {

    private val pathfindingStrategy = AStartPathfindingStrategy(PathfindingConfiguration())

    fun invoke(entityId: EntityId): Either<EntityException, World> =
        findWorld()
            .flatMap { world -> world.updateEntity(entityId) }
            .flatMap { world -> world.save() }

    private fun World.updateEntity(entityId: EntityId): Either<EntityException, World> =
        findEntity(entityId)
            .flatMap { entity -> entity.selectNextSection(this) }
            .map { entity -> entity.update() }
            .map { entity -> putEntity(entity) }
            .mapLeft { UnknownEntityException(it) }

    private fun Entity.selectNextSection(world: World): Either<EntityException, Entity> =
        if (shouldResumeRoute || shouldUpdateNextSection) {
            pathfindingStrategy.invoke(
                cells = world.connectedCells,
                source = world.getCell(currentPosition),
                target = world.getCell(nextDestination)
            ).map { result -> result.removeFirstCell() }
                .map { result -> updateNextSection(result.path) }
                .mapLeft { UnknownEntityException(it) }
        } else this.right()
}
