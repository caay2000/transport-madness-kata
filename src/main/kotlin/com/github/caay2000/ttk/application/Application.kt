package com.github.caay2000.ttk.application

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.application.entity.EntityCreatorService
import com.github.caay2000.ttk.application.world.WorldCreatorService
import com.github.caay2000.ttk.application.world.WorldUpdaterService
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.domain.world.WorldProvider
import com.github.caay2000.ttk.infra.provider.DefaultWorldProvider

class Application(
    private val configuration: Configuration,
    private val provider: WorldProvider = DefaultWorldProvider()
) {

    private val worldCreatorService = WorldCreatorService(provider)
    private val worldUpdaterService = WorldUpdaterService(provider)
    private val entityCreatorService = EntityCreatorService(provider)

    private fun createWorld(): World = worldCreatorService.invoke(configuration).bind()
    private fun updateWorld(): World = worldUpdaterService.invoke().bind()
    private fun createEntity(x: Int, y: Int): World = entityCreatorService.invoke(Position(x, y)).bind()

    fun invoke(): Int {

        createWorld()
        createEntity(0, 0)

        while (checkRouteCompleted().not()) {
            updateWorld()
            if (provider.get().bind().currentTurn > 100)
                return -1
        }

        return provider.get().bind().currentTurn
    }

    private fun checkRouteCompleted(): Boolean = provider.get().bind().entities.values.first().finished
}
