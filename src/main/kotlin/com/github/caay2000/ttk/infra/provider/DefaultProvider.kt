package com.github.caay2000.ttk.infra.provider

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.api.provider.Provider.ProviderException
import com.github.caay2000.ttk.api.provider.Provider.ProviderException.ConfigurationNotFoundProviderException
import com.github.caay2000.ttk.api.provider.Provider.ProviderException.UnknownProviderException
import com.github.caay2000.ttk.api.provider.Provider.ProviderException.WorldNotFoundProviderException
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.mapCatch

class DefaultProvider : Provider {

    private var world: World? = null
    private var configuration: Configuration? = null

    override fun get(): Either<ProviderException, World> =
        world.rightIfNotNull { WorldNotFoundProviderException }

    override fun set(world: World): Either<ProviderException, World> =
        Either.catch { this.world = world }
            .map { world }
            .mapLeft { UnknownProviderException(it) }

    override fun getLocation(position: Position): Either<ProviderException, Location> =
        get()
            .mapCatch { world -> world.locations.values.first { it.position == position } }
            .mapLeft { ProviderException.LocationNotFoundByPositionException(position) }

    override fun getEntity(entityId: EntityId): Either<ProviderException, Entity> =
        get()
            .map { world -> world.entities.values.first { it.id == entityId } }
            .mapLeft { ProviderException.EntityNotFoundException(entityId) }

    override fun getConfiguration(): Either<ProviderException, Configuration> =
        configuration.rightIfNotNull { ConfigurationNotFoundProviderException }

    override fun setConfiguration(configuration: Configuration): Either<ProviderException, Configuration> =
        Either.catch { this.configuration = configuration }
            .map { configuration }
            .mapLeft { UnknownProviderException(it) }
}