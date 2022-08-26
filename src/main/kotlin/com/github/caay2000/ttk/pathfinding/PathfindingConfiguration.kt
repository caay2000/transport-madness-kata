package com.github.caay2000.ttk.pathfinding

import com.github.caay2000.ttk.context.configuration.domain.Configuration

data class PathfindingConfiguration(
    val needConnection: Boolean = true
) {

    companion object {

        private var creteConnectionStrategyConfiguration: PathfindingConfiguration = PathfindingConfiguration()
        private var routeStrategyConfiguration: PathfindingConfiguration = PathfindingConfiguration()

        fun getCreteConnectionStrategyConfiguration(): PathfindingConfiguration = creteConnectionStrategyConfiguration
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
