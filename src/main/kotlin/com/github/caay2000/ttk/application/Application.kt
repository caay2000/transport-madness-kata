package com.github.caay2000.ttk.application

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.application.configuration.ConfigurationSetterService
import com.github.caay2000.ttk.application.entity.EntityCreatorService
import com.github.caay2000.ttk.application.entity.EntityRouteAssignerService
import com.github.caay2000.ttk.application.world.WorldCreatorService
import com.github.caay2000.ttk.application.world.WorldUpdaterService
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.infra.provider.DefaultProvider

class Application(
    private val configuration: Configuration,
    private val provider: Provider = DefaultProvider()
) {

    private val configurationSetterService = ConfigurationSetterService(provider)
    private val worldCreatorService = WorldCreatorService(provider)
    private val worldUpdaterService = WorldUpdaterService(provider)
    private val entityCreatorService = EntityCreatorService(provider)
    private val entityRouteAssignerService = EntityRouteAssignerService(provider)

    fun invoke(startPosition: Position, route: List<Position>): Int {

        configurationSetterService.invoke(configuration).bind()
        worldCreatorService.invoke().bind()
        entityCreatorService.invoke(startPosition).bind()
        entityRouteAssignerService.invoke(entity.id, route).bind()

        while (checkRouteCompleted(startPosition).not()) {
            worldUpdaterService.invoke().bind()
            println("${world.currentTurn} - $entity")
            if (world.currentTurn > 100)
                return -1
        }

        return world.currentTurn
    }

    private fun checkRouteCompleted(startPosition: Position): Boolean = world.currentTurn > 1 && entity.currentPosition == startPosition

    private val world: World
        get() = provider.get().bind()
    private val entity: Entity
        get() = provider.get().bind().entities.values.first()
}
