package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.CommandBus
import com.github.caay2000.ttk.api.event.Event

class KTCommandBus<in COMMAND : Command> : CommandBus<COMMAND> {

    override fun publish(command: COMMAND) = KTEventBus.getInstance<COMMAND, Event>().publishCommand(command)
}
