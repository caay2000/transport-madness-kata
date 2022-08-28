package com.github.caay2000.ttk.api.event

interface CommandBus {

    fun <COMMAND : Command> publish(command: COMMAND)
}
