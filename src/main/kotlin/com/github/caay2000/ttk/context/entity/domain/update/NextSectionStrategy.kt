package com.github.caay2000.ttk.context.entity.domain.update

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.pathfinding.AStartPathfindingStrategy
import com.github.caay2000.ttk.pathfinding.PathfindingConfiguration

sealed class NextSectionStrategy {

    abstract fun invoke(entity: Entity): Entity

    class SimpleNextSectionStrategy(private val provider: Provider) : NextSectionStrategy() {

        private val pathfindingStrategy = AStartPathfindingStrategy(PathfindingConfiguration())

        override fun invoke(entity: Entity): Entity {
            val world = provider.get().bind()
            val result = pathfindingStrategy.invoke(
                cells = world.connectedCells,
                source = world.getCell(entity.currentPosition),
                target = world.getCell(entity.route.currentDestination)
            ).bind()
            return entity.copy(route = entity.route.updateNextSection(result.removeFirstCell().path))
        }
    }
}
