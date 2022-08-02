package com.github.caay2000.ttk.application.world.update

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.application.world.WorldException
import com.github.caay2000.ttk.application.world.WorldService
import com.github.caay2000.ttk.domain.entity.NextSectionFinder
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World

class WorldUpdaterService(provider: Provider) : WorldService(provider) {

    fun invoke(): Either<WorldException, World> =
        findWorld()
            .map { world -> world.updateLocations() }
            .map { world -> world.updateEntities() }
            .map { world -> world.update() }
            .flatMap { world -> world.save() }

    private fun World.updateLocations(): World = locations.values
        .fold(this) { world, location ->
            location.update()
                .let { world.refreshLocation(it) }
        }

    private fun World.updateEntities(): World = entities.values
        .fold(this) { world, entity ->
            entity.update(NextSectionFinder(world))
                .let { world.refreshEntity(it) }
        }
}
