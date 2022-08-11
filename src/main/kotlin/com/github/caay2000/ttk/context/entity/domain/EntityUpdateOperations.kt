package com.github.caay2000.ttk.context.entity.domain

import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.Position

interface EntityUpdateOperations {

    fun getConfiguration(): Configuration
    fun entityNextSection(currentPosition: Position, currentDestination: Position): List<Cell>
    fun locationPassengerAvailable(currentPosition: Position): Int
}
