package com.github.caay2000.ttk.infra.database

@Suppress("UNCHECKED_CAST")
class InMemoryDatabase {

    private val database: MutableMap<String, Map<String, *>> = mutableMapOf()

    fun <T> save(table: String, key: String, value: T): T {
        val actualTable: Map<String, *>? = database[table]
        if (actualTable == null) {
            database[table] = mapOf(key to value)
        } else {
            database[table] = actualTable + mapOf(key to value)
        }

        return value
    }

    fun exists(table: String, key: String) = database[table]?.containsKey(key) ?: false

    fun <T> getById(table: String, id: String): T? = database[table]?.getValue(id) as T
    fun <T> getAll(table: String): List<T> = (database[table]?.values?.toList() ?: emptyList()) as List<T>
}
