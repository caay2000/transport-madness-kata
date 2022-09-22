package com.github.caay2000.ttk.context.pathfinding.domain

import com.github.caay2000.ttk.context.configuration.domain.Configuration

data class PathfindingConfiguration(
    val needConnection: Boolean = true
) {

    companion object {

        private var creteConnectionStrategyConfiguration: PathfindingConfiguration = PathfindingConfiguration()
        private var routeStrategyConfiguration: PathfindingConfiguration = PathfindingConfiguration()

        fun getCreateConnectionStrategyConfiguration(): PathfindingConfiguration = creteConnectionStrategyConfiguration
        fun getRouteStrategyConfiguration(): PathfindingConfiguration = routeStrategyConfiguration

        fun fromConfiguration(configuration: Configuration) {
            creteConnectionStrategyConfiguration = creteConnectionStrategyConfiguration.copy(
                needConnection = false
            )
            routeStrategyConfiguration = routeStrategyConfiguration.copy(
                needConnection = true
            )
        }
    }
}
