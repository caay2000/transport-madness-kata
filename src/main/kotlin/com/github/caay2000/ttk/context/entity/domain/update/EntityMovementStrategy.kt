package com.github.caay2000.ttk.context.entity.domain.update

import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.entity.domain.Entity

sealed class EntityMovementStrategy {

    abstract fun invoke(entity: Entity): Entity

    class SimpleEntityMovementStrategy(private val provider: Provider) : EntityMovementStrategy() {

        private val nextSectionStrategy: NextSectionStrategy = NextSectionStrategy.SimpleNextSectionStrategy(provider)

        override fun invoke(entity: Entity): Entity =
            if (entity.shouldUpdateNextSection()) nextSectionStrategy.invoke(entity).move()
            else entity.move()

        private fun Entity.shouldUpdateNextSection(): Boolean = route.nextSectionList.isEmpty()

        private fun Entity.move(): Entity = copy(currentPosition = route.nextSection.position, route = route.dropNextSection())
    }
}
