package com.github.caay2000.ttk.infra.provider

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.domain.world.WorldProvider

class DefaultWorldProvider : WorldProvider {

    private var world: World? = null

    override fun get(): Either<Throwable, World> = world.rightIfNotNull { RuntimeException("world does not exists") }

    override fun set(world: World): Either<Throwable, World> =
        Either.catch { this.world = world }
            .map { world }
}
