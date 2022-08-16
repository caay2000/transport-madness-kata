package com.github.caay2000.ttk.infra.console

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.provider.Provider
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.shared.LocationId

class HexagonalConsolePrinter(val provider: Provider, val configuration: Configuration) : Printer {

    val locations: Map<LocationId, String>
        get() = provider.get().bind().locations.mapValues { it.value.name[0].toString() }

    override fun print(world: World) {
        println("WORLD CURRENT TURN -> ${world.currentTurn} - ${world.entities.values.first()}")

        for (y in 0 until configuration.worldHeight) {
            var currentLine = if (y.mod(2) == 0) "" else " "
            for (x in 0 until configuration.worldWidth) {
                val cell = world.getCell(Position(x, y))
                val entity = world.entities.values.first()
                when {
                    entity.currentPosition == cell.position -> currentLine = "$currentLine@ "
                    cell.locationId != null -> currentLine = "${currentLine}${locations[cell.locationId]} "
                    cell.connected -> currentLine = "${currentLine}x "
                    cell.connected.not() -> currentLine = "$currentLine. "
                }
            }
            println(currentLine)
        }
        world.locations.forEach { (_, it) ->
            println("(${it.position.x}, ${it.position.y}) ${it.population} population - ${it.pax}PAX waiting - ${it.received} PAX Received")
        }
    }

    private fun printHeader() {
        var currentLine = ""
        for (x in 0 until configuration.worldWidth) {
            currentLine = "$currentLine $x "
        }
        println(currentLine)
    }
}
