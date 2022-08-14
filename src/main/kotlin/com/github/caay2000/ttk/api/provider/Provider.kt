package com.github.caay2000.ttk.api.provider

import arrow.core.Either
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.LocationId

interface Provider {

    fun get(): Either<ProviderException, World>
    fun set(world: World): Either<ProviderException, World>

    fun getLocation(position: Position): Either<ProviderException, Location>
    fun getEntity(entityId: EntityId): Either<ProviderException, Entity>

    fun getConfiguration(): Either<ProviderException, Configuration>
    fun setConfiguration(configuration: Configuration): Either<ProviderException, Configuration>

    sealed class ProviderException : RuntimeException {
        constructor() : super()
        constructor(message: String) : super(message)
        constructor(cause: Throwable) : super(cause)

        object WorldNotFoundProviderException : ProviderException()
        object ConfigurationNotFoundProviderException : ProviderException()

        data class LocationNotFoundByPositionException(val position: Position) : ProviderException("Location in position $position not found")
        data class LocationNotFoundByLocationIdException(val locationId: LocationId) : ProviderException("Location $locationId not found")
        data class EntityNotFoundException(val entityId: EntityId) : ProviderException("Entity $entityId not found")

        data class UnknownProviderException(override val cause: Throwable) : ProviderException(cause)
    }
}
