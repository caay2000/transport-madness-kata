package com.github.caay2000.ttk.infra.console

import com.github.caay2000.ttk.context.configuration.domain.Configuration
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World

class HexagonalConsolePrinter(val configuration: Configuration) : Printer {

    override fun print(world: World) {
        println("WORLD CURRENT TURN -> ${world.currentTurn} - ${world.entities.values.first()}")

        printHeader()
        for (y in 0 until configuration.worldHeight) {
            var currentLine = ""
            for (x in 0 until configuration.worldWidth) {

                val cell = world.getCell(Position(x, y))
                val entity = world.entities.values.first()
                when {
                    entity.currentPosition == cell.position -> currentLine = "$currentLine @ "
                    cell.locationId != null -> currentLine = "$currentLine H "
                    cell.connected -> currentLine = "$currentLine + "
                    cell.connected.not() -> currentLine = "$currentLine . "
                }
            }
            println(currentLine)
        }
        world.locations.forEach { (_, it) ->
            println("(${it.position.x}, ${it.position.y}) ${it.population} population - ${it.pax}PAX waiting - ${it.received} PAX Received")
        }
    }

    private fun printHeader() {
        var currentLine = "   - "
        for (x in 0 until configuration.worldWidth) {
            currentLine = "$currentLine $x "
        }
        println(currentLine)
    }
}
