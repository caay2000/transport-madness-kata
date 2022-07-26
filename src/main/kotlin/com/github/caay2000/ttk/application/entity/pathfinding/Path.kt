package com.github.caay2000.ttk.application.entity.pathfinding

import com.github.caay2000.ttk.domain.world.Cell

data class Path(val cells: Set<Cell>, val cost: Float)
