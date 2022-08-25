package com.github.caay2000.ttk.context.entity.domain.update

import arrow.core.computations.ResultEffect.bind
import arrow.core.flatMap
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.world.application.WorldRepository
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.pathfinding.AStartPathfindingStrategy
import com.github.caay2000.ttk.pathfinding.PathfindingConfiguration

sealed class NextSectionStrategy {

    abstract fun invoke(entity: Entity): Entity

    class SimpleNextSectionStrategy(private val worldRepository: WorldRepository) : NextSectionStrategy() {

        private val pathfindingStrategy = AStartPathfindingStrategy(PathfindingConfiguration())

        override fun invoke(entity: Entity): Entity =
            findWorld()
                .flatMap { world -> world.findNextSection(entity.currentPosition, entity.route.currentDestination) }
                .map { nextSection -> nextSection.removeFirstCell().path }
                .map { path -> entity.copy(route = entity.route.updateNextSection(path)) }
                .bind()

        private fun findWorld() = worldRepository.get()

        private fun World.findNextSection(source: Position, target: Position) =
            pathfindingStrategy.invoke(
                cells = connectedCells,
                source = getCell(source),
                target = getCell(target)
            )
    }
}
