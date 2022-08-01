package com.github.caay2000.ttk.application.entity.update

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.application.entity.EntityException
import com.github.caay2000.ttk.application.entity.UnknownEntityException
import com.github.caay2000.ttk.application.pathfinding.AStartPathfindingStrategy
import com.github.caay2000.ttk.application.pathfinding.PathfindingConfiguration
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.world.World

internal class EntityUpdaterInternalService {

    private val pathfindingStrategy = AStartPathfindingStrategy(PathfindingConfiguration())

    fun invoke(world: World, entity: Entity): Either<EntityException, World> =
        entity.selectNextSection(world)
            .map { entity -> entity.update() }
            .map { entity -> world.putEntity(entity) }
            .mapLeft { UnknownEntityException(it) }

    private fun Entity.selectNextSection(world: World): Either<EntityException, Entity> =
        Either.catch {
            updateNextSection {
                pathfindingStrategy.invoke(
                    cells = world.connectedCells,
                    source = world.getCell(currentPosition),
                    target = world.getCell(nextDestination)
                ).map { result -> result.removeFirstCell() }.bind()
            }
        }.mapLeft { UnknownEntityException(it) }
}
