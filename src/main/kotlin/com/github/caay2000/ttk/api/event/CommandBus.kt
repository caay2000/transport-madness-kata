package com.github.caay2000.ttk.api.event

interface CommandBus<in COMMAND : Command> {

    fun publish(command: COMMAND)
}
