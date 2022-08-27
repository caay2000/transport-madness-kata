package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.CommandHandler
import com.github.caay2000.ttk.api.event.Event
import kotlin.reflect.KClass

abstract class KTCommandHandler<in COMMAND : Command>(type: KClass<*>) : CommandHandler<COMMAND> {

    init {
        subscribeTo(type)
    }

    private fun subscribeTo(type: KClass<*>) {
        KTEventBus.getInstance<COMMAND, Event>().subscribe(this, type)
    }

    internal fun execute(command: Any) {
        @Suppress("UNCHECKED_CAST")
        this.invoke(command as COMMAND)
    }

    abstract override fun invoke(command: COMMAND)
}

fun <COMMAND : Command> instantiateCommandHandler(clazz: KClass<COMMAND>, commandHandler: CommandHandler<COMMAND>): KTCommandHandler<COMMAND> =
    object : KTCommandHandler<COMMAND>(clazz) {
        override fun invoke(command: COMMAND) = commandHandler.invoke(command)
    }
