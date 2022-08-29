package com.github.caay2000.ttk.context.world.application

import arrow.core.Either
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.world.domain.UnknownWorldException
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.WorldException

interface WorldServiceApi {

    fun find(): Either<WorldException, World>
    fun save(world: World): Either<WorldException, World>
    fun publishEvents(world: World): Either<WorldException, World>
}

fun worldService(worldRepository: WorldRepository, eventPublisher: EventPublisher) = object : WorldServiceApi {

    private val worldFinderService = WorldFinderService(worldRepository)

    override fun find(): Either<WorldException, World> =
        worldFinderService.invoke()

    override fun save(world: World): Either<WorldException, World> =
        worldRepository.save(world)
            .map { world }
            .mapLeft { UnknownWorldException(it) }

    override fun publishEvents(world: World): Either<WorldException, World> =
        Either.catch { eventPublisher.publish(world.pullEvents()).let { world } }
            .mapLeft { UnknownWorldException(it) }
}
