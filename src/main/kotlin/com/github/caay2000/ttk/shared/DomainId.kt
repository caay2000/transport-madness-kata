package com.github.caay2000.ttk.shared

import java.util.UUID
import kotlin.reflect.full.primaryConstructor

sealed class DomainId(uuid: UUID) {
    abstract val uuid: UUID
    val rawId = uuid.toString()
}

data class EntityId(override val uuid: UUID = UUID.randomUUID()) : DomainId(uuid)

inline fun <reified T : DomainId> randomDomainId() =
    UUID.randomUUID().toDomainId<T>()

inline fun <reified T : DomainId> UUID.toDomainId() =
    T::class.primaryConstructor!!.call(this)
