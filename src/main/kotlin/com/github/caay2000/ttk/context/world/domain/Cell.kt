package com.github.caay2000.ttk.context.world.domain

import com.github.caay2000.ttk.shared.LocationId

data class Cell(
    val position: Position,
    val connection: CellConnection,
    val locationId: LocationId? = null
) {
    constructor(x: Int, y: Int, connection: CellConnection = CellConnection.NOT_CONNECTED) : this(position = Position(x, y), connection)

    fun distance(target: Cell): Int = position.distance(target.position)
    fun samePosition(cell: Cell): Boolean = position == cell.position
    fun createConnection(): Cell = copy(connection = CellConnection.CONNECTED)
    fun updateLocationId(locationId: LocationId): Cell = this.copy(locationId = locationId)

    val connected: Boolean
        get() = connection != CellConnection.NOT_CONNECTED

    enum class CellConnection(val cost: Int) {
        NOT_CONNECTED(101),
        CONNECTED(100)
    }
}
