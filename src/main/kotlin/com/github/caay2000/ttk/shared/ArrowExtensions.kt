package com.github.caay2000.ttk.shared

import arrow.core.Either

fun <B, C> Either<Throwable, B>.mapCatch(f: (B) -> C): Either<Throwable, C> =
    when (this) {
        is Either.Right -> Either.catch { f(this.value) }
        is Either.Left -> this
    }
