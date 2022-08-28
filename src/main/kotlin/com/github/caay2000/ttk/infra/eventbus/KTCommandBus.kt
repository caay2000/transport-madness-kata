package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.CommandBus
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.Query

class KTCommandBus : CommandBus {

    override fun <COMMAND : Command> publish(command: COMMAND) = KTEventBus.getInstance<COMMAND, Query, Event>().publishCommand(command)
}
