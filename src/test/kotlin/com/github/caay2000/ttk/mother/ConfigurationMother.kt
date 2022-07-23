package com.github.caay2000.ttk.mother

import com.github.caay2000.ttk.domain.configuration.Configuration
import java.util.Random

object ConfigurationMother {

    fun random(
        worldWidth: Int = Random().nextInt(3, 5),
        worldHeight: Int = worldWidth + 1,
    ): Configuration =
        Configuration(
            worldWidth = worldWidth,
            worldHeight = worldHeight
        )
}
