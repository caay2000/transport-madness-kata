package com.github.caay2000.ttk.context.location.secondary

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.location.application.LocationRepository
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import com.github.caay2000.ttk.shared.LocationId

class InMemoryLocationRepository(private val db: InMemoryDatabase) : LocationRepository {

    private val byPositionIndex: MutableMap<Position, LocationId> = mutableMapOf()

    override fun exists(criteria: LocationRepository.FindLocationCriteria): Boolean =
        when (criteria) {
            is LocationRepository.FindLocationCriteria.ById ->
                db.exists(TABLE_NAME, criteria.id.rawId)
            is LocationRepository.FindLocationCriteria.ByPosition ->
                db.exists(TABLE_NAME, byPositionIndex[criteria.position]?.rawId ?: "")
        }

    override fun find(criteria: LocationRepository.FindLocationCriteria): Either<Throwable, Location> =
        Either.catch {
            when (criteria) {
                is LocationRepository.FindLocationCriteria.ById ->
                    db.getById<Location>(TABLE_NAME, criteria.id.rawId)
                is LocationRepository.FindLocationCriteria.ByPosition ->
                    db.getById(TABLE_NAME, byPositionIndex[criteria.position]?.rawId ?: "")
            }
        }.flatMap { it?.right() ?: NoSuchElementException().left() }

    override fun findAll(): Either<Throwable, List<Location>> =
        Either.catch { db.getAll(TABLE_NAME) }

    override fun save(location: Location): Either<Throwable, Location> =
        Either.catch {
            db.save(TABLE_NAME, location.id.rawId, location)
            byPositionIndex[location.position] = location.id
        }.map { location }

    companion object {
        const val TABLE_NAME = "location"
    }

    init {
        findAll().tap { list -> list.forEach { byPositionIndex[it.position] = it.id } }
    }
}
