package com.github.caay2000.ttk.context.entity.secondary

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.entity.application.EntityRepository
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.infra.database.InMemoryDatabase

class InMemoryEntityRepository(private val db: InMemoryDatabase) : EntityRepository {

    override fun exists(criteria: EntityRepository.FindEntityCriteria): Boolean =
        when (criteria) {
            is EntityRepository.FindEntityCriteria.ById ->
                db.exists(TABLE_NAME, criteria.id.rawId)
        }

    override fun find(criteria: EntityRepository.FindEntityCriteria): Either<Throwable, Entity> =
        Either.catch {
            when (criteria) {
                is EntityRepository.FindEntityCriteria.ById ->
                    db.getById<Entity>(TABLE_NAME, criteria.id.rawId)
            }
        }.flatMap { it?.right() ?: NoSuchElementException().left() }

    override fun findAll(): Either<Throwable, List<Entity>> = Either.catch { db.getAll(TABLE_NAME) }

    override fun findAll(criteria: EntityRepository.FindAllCriteria): Either<Throwable, List<Entity>> =
        Either.catch {
            when (criteria) {
                is EntityRepository.FindAllCriteria.ByCompanyId ->
                    db.getAll<Entity>(TABLE_NAME).filter { it.companyId == criteria.companyId }
            }
        }

    override fun save(entity: Entity): Either<Throwable, Entity> =
        Either.catch {
            db.save(TABLE_NAME, entity.id.rawId, entity)
        }.map { entity }

    companion object {
        const val TABLE_NAME = "entity"
    }
}
