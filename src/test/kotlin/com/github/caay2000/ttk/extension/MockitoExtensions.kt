package com.github.caay2000.ttk.extension

import arrow.core.Either
import org.mockito.stubbing.OngoingStubbing

inline fun <reified T> OngoingStubbing<*>.thenReturnFirstArgument(crossinline block: (T) -> Either<Nothing, T>) {
    this.then { i -> block(i.getArgument(0, T::class.java)) }
}
