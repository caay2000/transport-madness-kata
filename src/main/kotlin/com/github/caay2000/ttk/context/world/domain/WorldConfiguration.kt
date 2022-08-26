package com.github.caay2000.ttk.context.world.domain

import com.github.caay2000.ttk.context.configuration.domain.Configuration

data class WorldConfiguration(
    val worldWidth: Int = 6,
    val worldHeight: Int = 6
) {

    companion object {

        private var instance: WorldConfiguration = WorldConfiguration()

        fun get(): WorldConfiguration = instance

        fun fromConfiguration(configuration: Configuration): WorldConfiguration {
            instance = instance.copy(
                worldWidth = configuration.worldWidth,
                worldHeight = configuration.worldHeight
            )
            return instance
        }
    }
}
