package com.github.caay2000.ttk.application.pathfinding

sealed class PathfindingException : RuntimeException {
    constructor(cause: Throwable) : super(cause)
}

data class UnknownPathfindingException(override val cause: Throwable) : PathfindingException(cause)
