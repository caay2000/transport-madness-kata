package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.CommandBus
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.Query

class KTCommandBus<in COMMAND : Command> : CommandBus<COMMAND> {

    override fun publish(command: COMMAND) = KTEventBus.getInstance<COMMAND, Query, Event>().publishCommand(command)
}
