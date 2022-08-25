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

    override fun findAll(): Either<Throwable, List<World>> =
        Either.catch { db.getAll(TABLE_NAME) }

    override fun save(world: World): Either<Throwable, World> =
        Either.catch {
            db.save(TABLE_NAME, world.id.rawId, world)
        }.map { world }

    companion object {
        const val TABLE_NAME = "world"
        const val WORLD_ID = "1"
    }
}
