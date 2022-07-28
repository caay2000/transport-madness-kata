package com.github.caay2000.ttk

import com.github.caay2000.ttk.application.Application
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.infra.provider.DefaultProvider

class App {

    fun invoke(src: String, dest: String): Int {

        val configuration = Configuration(4, 6)
        val provider = DefaultProvider()

        val application = Application(configuration, provider)

        return application.invoke(Position(0, 0), listOf(Position(0, 0)))
    }
}

fun main(args: Array<String>) {
//    println(App().invoke())
}
