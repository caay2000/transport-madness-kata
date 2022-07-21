package com.github.caay2000.ttk.mother

import com.github.caay2000.ttk.domain.configuration.Configuration
import java.util.Random

object ConfigurationMother {

    fun random(
        worldWidth: Int = Random().nextInt(3) + 4, // from 4 to 6
        worldHeight: Int = worldWidth + 1, // from 5 to 7
    ): Configuration = Configuration(
        worldWidth = worldWidth,
        worldHeight = worldHeight
    )
}
