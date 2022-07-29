package com.github.caay2000.ttk.application.world

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.world.Location
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World

class WorldUpdaterService(provider: Provider) : WorldService(provider) {

    fun invoke(): Either<WorldException, World> =
        findWorld()
            .flatMap { world -> world.doUpdate() }
            .flatMap { world -> world.save() }

    private fun World.doUpdate(): Either<WorldException, World> =
        Either.catch { updateLocations() }
            .map { world -> world.updateEntities() }
            .map { world -> world.update() }
            .mapLeft { UnknownWorldException(it) }

    private fun World.updateLocations() = locations.values.fold(this) { world, location -> world.updateLocation(location) }
    private fun World.updateLocation(location: Location): World =
        this.getLocation(location.id).update()
            .let { location -> this.putLocation(location) }

    private fun World.updateEntities() = entities.values.fold(this) { world, entity -> world.updateEntity(entity) }
    private fun World.updateEntity(entity: Entity): World =
        this.getEntity(entity.id).update()
            .let { entity -> this.putEntity(entity) }
}
