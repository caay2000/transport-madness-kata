package com.github.caay2000.ttk.domain.configuration

data class Configuration(

    /* WORLD VALUES */
    val worldWidth: Int = 4,
    val worldHeight: Int = 6,

    /* STOP VALUES */
    val turnsStoppedInStation: Int = 1,

    /* CITY VALUES */
    val minDistanceBetweenCities: Int = 1,
    val cityPAXPercentage: Double = 0.002
)
