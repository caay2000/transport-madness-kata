package com.github.caay2000.ttk.application.entity.update

import arrow.core.Either
import com.github.caay2000.ttk.application.entity.EntityException
import com.github.caay2000.ttk.application.entity.UnknownEntityException
import com.github.caay2000.ttk.domain.entity.Entity
import com.github.caay2000.ttk.domain.entity.NextSectionFinder
import com.github.caay2000.ttk.domain.world.World

internal class EntityUpdaterInternalService {

    fun invoke(world: World, entity: Entity): Either<EntityException, World> =
        Either.catch { entity.update(NextSectionFinder(world)) }
            .map { entity -> world.putEntity(entity) }
            .mapLeft { UnknownEntityException(it) }
}
