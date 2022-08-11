package com.github.caay2000.ttk.context.entity.query

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.getOrHandle
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.world.domain.Position

class PassengerLoadingSystem(private val provider: Provider) {

    fun invoke(position: Position): Int =
        Either.catch {
            provider.get().bind().let { world ->
                world.getLocation(position).pax
            }
        }.getOrHandle { 0 }
}
