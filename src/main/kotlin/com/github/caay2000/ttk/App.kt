package com.github.caay2000.ttk

import com.github.caay2000.ttk.application.Application
import com.github.caay2000.ttk.domain.configuration.Configuration
import com.github.caay2000.ttk.infra.provider.DefaultWorldProvider

class App {

    fun invoke(src: String, dest: String): Int {

        val configuration = Configuration(4, 6)
        val provider = DefaultWorldProvider()

        val application = Application(configuration, provider)

        return application.invoke()
    }
}

fun main(args: Array<String>) {
//    println(App().invoke())
}
