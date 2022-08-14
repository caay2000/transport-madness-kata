package com.github.caay2000.ttk.context.location.domain

import com.github.caay2000.ttk.context.configuration.domain.Configuration

data class LocationConfiguration(
    val cityPAXPercentage: Double = 0.002,
    val cityDefaultWaitingTurnsForEntities: Int = 1
) {

    companion object {
        fun fromConfiguration(configuration: Configuration): LocationConfiguration =
            LocationConfiguration(
                cityPAXPercentage = configuration.cityPAXPercentage,
                cityDefaultWaitingTurnsForEntities = configuration.turnsStoppedInStation
            )
    }
}
