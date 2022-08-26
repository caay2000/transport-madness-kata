package com.github.caay2000.ttk.mother

import com.github.caay2000.ttk.context.configuration.application.ConfigurationSetterService
import com.github.caay2000.ttk.context.configuration.domain.Configuration
import java.util.Random

object ConfigurationMother {

    fun random(
        worldWidth: Int = Random().nextInt(3) + 4, // from 4 to 6
        worldHeight: Int = worldWidth + 1, // from 5 to 7
        turnsStoppedInStation: Int = 1,
        minDistanceBetweenCities: Int = 6,
        cityPAXPercentage: Double = 0.002
    ): Configuration = Configuration(
        worldWidth = worldWidth,
        worldHeight = worldHeight,
        turnsStoppedInStation = turnsStoppedInStation,
        minDistanceBetweenCities = minDistanceBetweenCities,
        cityPAXPercentage = cityPAXPercentage
    )
}

fun Configuration.set(): Configuration {
    ConfigurationSetterService().invoke(this)
    return this
}
