package com.github.caay2000.ttk.shared

fun <T> Collection<T>.replace(
    predicate: (T) -> Boolean,
    operation: (T) -> (T)
): Collection<T> = map { if (predicate(it)) operation(it) else it }

inline fun <T, R> Iterable<T>.mapToSet(transform: (T) -> R): Set<R> {
    return mapTo(HashSet(), transform)
}
