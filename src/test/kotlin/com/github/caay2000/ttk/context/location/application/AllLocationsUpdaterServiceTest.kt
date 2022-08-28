package com.github.caay2000.ttk.context.location.application

import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.domain.UnknownLocationException
import com.github.caay2000.ttk.extension.thenReturnFirstArgument
import com.github.caay2000.ttk.mother.world.location.LocationMother
import io.kotest.assertions.arrow.either.shouldBeLeftOfType
import io.kotest.assertions.arrow.either.shouldBeRight
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class AllLocationsUpdaterServiceTest {

    private val locationRepository: LocationRepository = mock()
    private val eventPublisher: EventPublisher = mock()
    private val sut = AllLocationsUpdaterService(locationRepository, eventPublisher)

    @ParameterizedTest(name = "location with {0} population updated its rawPAX correctly")
    @CsvSource(value = ["1000,2.0", "500,1.0", "250,0.5"])
    fun `location rawPAX is updated correctly`(population: Int, rawPAX: Double) {

        val location = LocationMother.random(population = population)
        `location exists`(location)
        `location will be updated`()

        sut.invoke().shouldBeRight {
            verify(locationRepository).save(location.copy(rawPAX = rawPAX))
        }
    }

    @Test
    fun `throws exception if any location fails to update`() {

        val workingLocation = LocationMother.random()
        val failingLocation = LocationMother.random(population = 0)
        `location exists`(workingLocation, failingLocation, LocationMother.random())
        `location will be updated`()

        `some location will fail to update`(failingLocation)

        sut.invoke().shouldBeLeftOfType<UnknownLocationException>()
        verify(locationRepository, times(2)).save(any())
    }

    private fun `location exists`(vararg location: Location) {
        whenever(locationRepository.findAll()).thenReturn(listOf(*location).right())
    }

    private fun `location will be updated`() {
        whenever(locationRepository.save(any())).thenReturnFirstArgument<Location> { it.right() }
    }

    private fun `some location will fail to update`(location: Location) {
        whenever(locationRepository.save(location)).thenReturn(RuntimeException().left())
    }
}
