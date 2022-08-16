package com.github.caay2000.ttk.shared

fun <T> Collection<T>.replace(
    predicate: (T) -> Boolean,
    operation: (T) -> (T)
): Collection<T> = map { if (predicate(it)) operation(it) else it }
