package com.github.caay2000.ttk.application.world.update

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.flatMap
import com.github.caay2000.ttk.application.entity.update.EntityUpdaterInternalService
import com.github.caay2000.ttk.application.world.UnknownWorldException
import com.github.caay2000.ttk.application.world.WorldException
import com.github.caay2000.ttk.application.world.WorldService
import com.github.caay2000.ttk.domain.world.Location
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World

class WorldUpdaterService(provider: Provider) : WorldService(provider) {

    private val entityUpdaterInternalService = EntityUpdaterInternalService()

    fun invoke(): Either<WorldException, World> =
        findWorld()
            .flatMap { world -> world.doUpdate() }
            .flatMap { world -> world.save() }

    private fun World.doUpdate(): Either<WorldException, World> =
        Either.catch { updateLocations() }
            .map { world -> world.updateEntities() }
            .map { world -> world.update() }
            .mapLeft { UnknownWorldException(it) }

    private fun World.updateLocation(location: Location): World = putLocation(location.update())
    private fun World.updateLocations() = locations.values
        .fold(this) { world, location -> world.updateLocation(location) }

    private fun World.updateEntities() = entities.values
        .fold(this) { world, entity -> entityUpdaterInternalService.invoke(world, entity).bind() }
}
