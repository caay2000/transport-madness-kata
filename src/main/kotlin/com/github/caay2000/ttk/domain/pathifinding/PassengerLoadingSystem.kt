package com.github.caay2000.ttk.domain.pathifinding

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.getOrHandle
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.Provider

class PassengerLoadingSystem(private val provider: Provider) {

    fun invoke(position: Position): Int =
        Either.catch {
            provider.get().bind().let { world ->
                world.getLocation(position).pax
            }
        }.getOrHandle { 0 }
}
