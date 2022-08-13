package com.github.caay2000.ttk.infra.console

import com.github.caay2000.ttk.context.world.domain.World

interface Printer {

    fun print(world: World)
}
