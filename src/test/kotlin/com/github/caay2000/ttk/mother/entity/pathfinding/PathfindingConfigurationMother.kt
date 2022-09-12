package com.github.caay2000.ttk.mother.entity.pathfinding

import com.github.caay2000.ttk.context.pathfinding.domain.PathfindingConfiguration

object PathfindingConfigurationMother {

    fun default(needConnection: Boolean = true) = PathfindingConfiguration(needConnection)
}
