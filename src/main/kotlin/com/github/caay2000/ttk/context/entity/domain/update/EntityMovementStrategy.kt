package com.github.caay2000.ttk.context.entity.domain.update

import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.world.application.WorldRepository

sealed class EntityMovementStrategy {

    abstract fun invoke(entity: Entity): Entity

    class SimpleEntityMovementStrategy(worldRepository: WorldRepository) : EntityMovementStrategy() {

        private val nextSectionStrategy: NextSectionStrategy = NextSectionStrategy.SimpleNextSectionStrategy(worldRepository)

        override fun invoke(entity: Entity): Entity =
            if (entity.shouldUpdateNextSection()) nextSectionStrategy.invoke(entity).move()
            else entity.move()

        private fun Entity.shouldUpdateNextSection(): Boolean = route.nextSectionList.isEmpty()

        private fun Entity.move(): Entity = copy(currentPosition = route.nextSection.position, route = route.dropNextSection())
    }
}
