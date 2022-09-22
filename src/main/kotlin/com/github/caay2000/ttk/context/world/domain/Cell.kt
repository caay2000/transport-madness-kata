package com.github.caay2000.ttk.context.world.domain

import com.github.caay2000.ttk.shared.CompanyId
import com.github.caay2000.ttk.shared.LocationId

data class Cell(
    val position: Position,
    val connection: CellConnection,
    val locationId: LocationId? = null
) {
    constructor(x: Int, y: Int, connection: CellConnection = NotConnetedCell) : this(position = Position(x, y), connection)

    fun distance(target: Cell): Int = position.distance(target.position)
    fun samePosition(cell: Cell): Boolean = position == cell.position
    fun createConnection(companyId: CompanyId): Cell = copy(connection = ConnectedCell(companyId))
    fun updateLocationId(locationId: LocationId): Cell = this.copy(locationId = locationId)

    val connected: Boolean
        get() = connection != NotConnetedCell

    sealed class CellConnection(val cost: CellConnectionCost)

    object NotConnetedCell : CellConnection(CellConnectionCost.NOT_CONNECTED)
    data class ConnectedCell(val companyId: CompanyId) : CellConnection(CellConnectionCost.CONNECTED)

    enum class CellConnectionCost(val value: Int) {
        NOT_CONNECTED(101),
        CONNECTED(100);

        operator fun plus(cellConnectionCost: CellConnectionCost): Int {
            return this.value + cellConnectionCost.value
        }
    }
}
