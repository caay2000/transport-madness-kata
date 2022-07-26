package com.github.caay2000.ttk.application.world

import arrow.core.Either
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.domain.world.Provider
import com.github.caay2000.ttk.domain.world.World

class WorldConnectionCreatorService(provider: Provider) : WorldService(provider) {

    fun invoke(source: Position, target: Position): Either<WorldException, World> =
        findWorld()
//            .flatMap { world -> world.createConnection(source, target) }
//            .flatMap { world -> world.save() }

    private fun World.createConnection(source: Position, target: Position) {
        TODO("Not yet implemented")
    }
}
