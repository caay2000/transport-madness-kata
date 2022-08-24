package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.shared.LocationId

interface LocationRepository {

    fun exists(criteria: FindLocationCriteria): Boolean
    fun find(criteria: FindLocationCriteria): Either<Throwable, Location>
    fun findAll(): Either<Throwable, List<Location>>
    fun save(location: Location): Either<Throwable, Location>

    sealed class FindLocationCriteria {

        class ById(val id: LocationId) : FindLocationCriteria()
        class ByPosition(val position: Position) : FindLocationCriteria()
    }
}
