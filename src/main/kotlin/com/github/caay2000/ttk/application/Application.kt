package com.github.caay2000.ttk.application

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.application.entity.EntityCreatorService
import com.github.caay2000.ttk.application.entity.EntityDestinationAssignerService
import com.github.caay2000.ttk.application.world.WorldCreatorService
import com.github.caay2000.ttk.application.world.WorldUpdaterService
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.WorldProvider
import com.github.caay2000.ttk.infra.provider.DefaultWorldProvider

class Application(
    private val configuration: Configuration,
    private val provider: WorldProvider = DefaultWorldProvider()
) {

    private val worldCreatorService = WorldCreatorService(provider)
    private val worldUpdaterService = WorldUpdaterService(provider)
    private val entityCreatorService = EntityCreatorService(provider)
    private val entityDestinationAssignerService = EntityDestinationAssignerService(provider)

    fun invoke(startPosition: Position, destination: Position): Int {

        worldCreatorService.invoke(configuration).bind()
        val world = entityCreatorService.invoke(startPosition).bind()
        val entityId = world.entities.keys.first()
        entityDestinationAssignerService.invoke(entityId, destination)

        while (checkRouteCompleted().not()) {
            worldUpdaterService.invoke().bind()
            if (provider.get().bind().currentTurn > 100)
                return -1
        }

        return provider.get().bind().currentTurn
    }

    private fun checkRouteCompleted(): Boolean = provider.get().bind().entities.values.first().finished
}
