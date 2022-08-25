package com.github.caay2000.ttk.infra.console

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.entity.application.EntityRepository
import com.github.caay2000.ttk.context.entity.domain.Entity
import com.github.caay2000.ttk.context.location.application.LocationRepository
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.shared.EntityId
import com.github.caay2000.ttk.shared.LocationId

class HexagonalConsolePrinter(
    private val locationRepository: LocationRepository,
    private val entityRepository: EntityRepository,
    val configuration: Configuration
) : Printer {

    val locations: Map<LocationId, Location>
        get() = locationRepository.findAll().bind().associateBy { it.id }

    val entities: Map<EntityId, Entity>
        get() = entityRepository.findAll().bind().associateBy { it.id }

    override fun print(world: World) {
        println("WORLD CURRENT TURN -> ${world.currentTurn} - ${entities.values.first()}")

        for (y in 0 until configuration.worldHeight) {
            var currentLine = if (y.mod(2) == 0) "" else " "
            for (x in 0 until configuration.worldWidth) {
                val cell = world.getCell(Position(x, y))
                val entity = entities.values.first()
                when {
                    entity.currentPosition == cell.position -> currentLine = "$currentLine@ "
                    cell.locationId != null -> currentLine = "${currentLine}${locations[cell.locationId]!!.name[0]} "
                    cell.connected -> currentLine = "${currentLine}x "
                    cell.connected.not() -> currentLine = "$currentLine. "
                }
            }
            println(currentLine)
        }
        locations.forEach { (_, it) ->
            println("(${it.position.x}, ${it.position.y}) ${it.population} population - ${it.pax}PAX waiting - ${it.received} PAX Received")
        }
    }
}
