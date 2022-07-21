package com.github.caay2000.ttk.mother

import com.github.caay2000.ttk.domain.configuration.Configuration
import java.util.Random

object ConfigurationMother {

    fun random(
        worldWidth: Int = Random().nextInt(1, 5),
        worldHeight: Int = Random().nextInt(1, 5),
    ): Configuration =
        Configuration(
            worldWidth = worldWidth,
            worldHeight = worldHeight
        )
}
