package com.github.caay2000.ttk.pathfinding

sealed class PathfindingException(cause: Throwable) : RuntimeException(cause)

data class UnknownPathfindingException(override val cause: Throwable) : PathfindingException(cause)
