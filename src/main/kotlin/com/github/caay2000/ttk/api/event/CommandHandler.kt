package com.github.caay2000.ttk.api.event

interface CommandHandler<in COMMAND : Command> {

    fun invoke(command: COMMAND)
}
