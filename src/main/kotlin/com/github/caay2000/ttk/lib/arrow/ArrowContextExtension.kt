package com.github.caay2000.ttk.lib.arrow

import arrow.core.Either

fun <LEFT, RIGHT_A, RIGHT_B> Either<LEFT, RIGHT_A>.context(first: RIGHT_B): Either<LEFT, Pair<RIGHT_B, RIGHT_A>> = this.map { first to it }

inline fun <A, B, C> Either<A, B>.pairMap(f: (B) -> Either<A, C>): Either<A, Pair<B, C>> =
    when (this) {
        is Either.Right -> f(this.value).context(this.value)
        is Either.Left -> this
    }
