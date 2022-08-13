package com.github.caay2000.ttk.context.entity.domain.update

import arrow.core.flatMap
import arrow.core.getOrHandle
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.pathfinding.AStartPathfindingStrategy
import com.github.caay2000.ttk.pathfinding.PathfindingConfiguration

sealed class NextSectionStrategy {

    abstract fun invoke(entity: Entity): Entity

    class SimpleNextSectionStrategy(private val provider: Provider) : NextSectionStrategy() {

        private val pathfindingStrategy = AStartPathfindingStrategy(PathfindingConfiguration())

        override fun invoke(entity: Entity): Entity =
            findWorld()
                .flatMap { world -> world.findNextSection(entity.currentPosition, entity.route.currentDestination) }
                .map { nextSection -> nextSection.removeFirstCell().path }
                .map { path -> entity.copy(route = entity.route.updateNextSection(path)) }
                .getOrHandle { entity }

        private fun findWorld() = provider.get()

        private fun World.findNextSection(source: Position, target: Position) =
            pathfindingStrategy.invoke(
                cells = connectedCells,
                source = getCell(source),
                target = getCell(target)
            )
    }
}
