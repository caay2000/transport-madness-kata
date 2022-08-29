package com.github.caay2000.ttk.context.entity.application.update

import arrow.core.Either
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.entity.domain.EntityException
import com.github.caay2000.ttk.context.entity.domain.EntityUpdateStopperServiceEntityException

class EntityUpdateStopperService {

    fun invoke(initialEntity: Entity): Either<EntityException, Entity> =
        Either.catch { initialEntity.updateStop() }
            .mapLeft { EntityUpdateStopperServiceEntityException(it) }
}
