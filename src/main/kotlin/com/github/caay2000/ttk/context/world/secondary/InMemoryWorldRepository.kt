package com.github.caay2000.ttk.context.world.secondary

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.world.application.WorldRepository
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.infra.database.InMemoryDatabase

class InMemoryWorldRepository(private val db: InMemoryDatabase) : WorldRepository {

    override fun get(): Either<Throwable, World> =
        Either.catch { db.getById<World>(TABLE_NAME, WORLD_ID) }
            .flatMap { it?.right() ?: NoSuchElementException().left() }

    override fun save(world: World): Either<Throwable, World> =
        Either.catch {
            db.save(TABLE_NAME, WORLD_ID, world)
        }.map { world }

    companion object {
        const val TABLE_NAME = "world"
        const val WORLD_ID = "1"
    }
}
