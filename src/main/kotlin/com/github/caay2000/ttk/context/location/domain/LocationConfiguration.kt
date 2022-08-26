package com.github.caay2000.ttk.context.location.domain

import com.github.caay2000.ttk.context.configuration.domain.Configuration

data class LocationConfiguration(
    val minDistanceBetweenCities: Int = 6,
    val cityPAXPercentage: Double = 0.002,
    val cityDefaultWaitingTurnsForEntities: Int = 1
) {

    companion object {

        private var instance: LocationConfiguration = LocationConfiguration()

        fun get(): LocationConfiguration = instance

        fun fromConfiguration(configuration: Configuration): LocationConfiguration {
            instance = instance.copy(
                minDistanceBetweenCities = configuration.minDistanceBetweenCities,
                cityPAXPercentage = configuration.cityPAXPercentage,
                cityDefaultWaitingTurnsForEntities = configuration.turnsStoppedInStation
            )
            return instance
        }
    }
}
