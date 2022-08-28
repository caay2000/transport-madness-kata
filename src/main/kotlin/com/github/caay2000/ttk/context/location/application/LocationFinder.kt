package com.github.caay2000.ttk.context.location.application

import arrow.core.Either
import com.github.caay2000.ttk.context.location.application.LocationRepository.FindLocationCriteria.ByIdCriteria
import com.github.caay2000.ttk.context.location.application.LocationRepository.FindLocationCriteria.ByPositionCriteria
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.domain.LocationException
import com.github.caay2000.ttk.context.location.domain.LocationNotFoundByLocationIdException
import com.github.caay2000.ttk.context.location.domain.LocationNotFoundByPositionException
import com.github.caay2000.ttk.context.location.domain.UnknownLocationException

class LocationFinder(private val locationRepository: LocationRepository) {

    fun invoke(criteria: LocationRepository.FindLocationCriteria): Either<LocationException, Location> =
        locationRepository.find(criteria)
            .mapLeft {
                when (it) {
                    is NoSuchElementException -> when (criteria) {
                        is ByIdCriteria -> LocationNotFoundByLocationIdException(criteria.id)
                        is ByPositionCriteria -> LocationNotFoundByPositionException(criteria.position)
                    }
                    else -> UnknownLocationException(it)
                }
            }
}
