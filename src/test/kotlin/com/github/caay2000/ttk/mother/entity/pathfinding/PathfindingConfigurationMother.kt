package com.github.caay2000.ttk.mother.entity.pathfinding

import com.github.caay2000.ttk.pathfinding.PathfindingConfiguration

object PathfindingConfigurationMother {

    fun default(needConnection: Boolean = true) = PathfindingConfiguration(needConnection)
}
