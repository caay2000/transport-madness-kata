package com.github.caay2000.ttk.domain.pathifinding

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.Provider

class PassengerLoadingSystem(private val provider: Provider) {

    fun invoke(position: Position): Int =
        provider.get().bind().let { world ->
            world.locations[world.getCell(position).locationId]?.pax ?: 0
        }
}
