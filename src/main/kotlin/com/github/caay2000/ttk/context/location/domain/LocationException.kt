package com.github.caay2000.ttk.context.location.domain

import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.shared.LocationId

sealed class LocationException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

data class LocationAlreadyExists(val position: Position) : LocationException("Location $position already exists")
data class LocationsTooCloseException(val position: Position) : LocationException("Location $position is too close to another location")
data class LocationNotFoundByPositionException(val position: Position) : LocationException("location in position $position not found")
data class LocationNotFoundByLocationIdException(val locationId: LocationId) : LocationException("location $locationId not found")
data class UnknownLocationException(override val cause: Throwable) : LocationException(cause)
