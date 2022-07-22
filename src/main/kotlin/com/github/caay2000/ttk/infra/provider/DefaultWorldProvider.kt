package com.github.caay2000.ttk.infra.provider

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.github.caay2000.ttk.domain.world.World
import com.github.caay2000.ttk.domain.world.WorldProvider
import com.github.caay2000.ttk.domain.world.WorldProvider.ProviderException

class DefaultWorldProvider : WorldProvider {

    private var world: World? = null

    override fun get(): Either<ProviderException, World> = world.rightIfNotNull { ProviderException("World does not found") }
    override fun set(world: World): Either<ProviderException, World> =
        Either.catch { this.world = world }
            .map { world }
            .mapLeft { ProviderException(it) }
}
