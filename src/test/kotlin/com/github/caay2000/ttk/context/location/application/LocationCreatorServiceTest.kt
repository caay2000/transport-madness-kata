package com.github.caay2000.ttk.context.location.application

import arrow.core.right
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.context.location.domain.LocationsTooCloseException
import com.github.caay2000.ttk.extension.thenReturnFirstArgument
import com.github.caay2000.ttk.mother.set
import com.github.caay2000.ttk.mother.world.location.LocationMother
import io.kotest.assertions.arrow.either.shouldBeLeftOfType
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class LocationCreatorServiceTest {

    private val locationRepository: LocationRepository = mock()
    private val eventPublisher: EventPublisher<Event> = mock()
    private val sut = LocationCreatorService(locationRepository, eventPublisher)

    @Test
    fun `city is created correctly`() {

        `no other locations exists`()
        `location will be created`()

        sut.invoke(location.name, location.position, location.population).shouldBeRight {
            verify(locationRepository).save(it)
            assertThat(it.position).isEqualTo(location.position)
            assertThat(it.population).isEqualTo(location.population)
        }
    }

    @Test
    fun `city creation fails if distance between cities is less than configuration$minDistanceBetweenCities`() {

        `another location too close already exists`()

        sut.invoke(location.name, location.position, location.population).shouldBeLeftOfType<LocationsTooCloseException>()

        verify(locationRepository, never()).save(any())
    }

    private fun `no other locations exists`() {
        whenever(locationRepository.findAll()).thenReturn(emptyList<Location>().right())
    }

    private fun `another location too close already exists`() {
        whenever(locationRepository.findAll()).thenReturn(listOf(tooCloseLocation).right())
    }

    private fun `location will be created`() {
        whenever(locationRepository.save(any())).thenReturnFirstArgument<Location> { it.right() }
    }

    private val location = LocationMother.random()
    private val tooCloseLocation = LocationMother.random(position = location.position.neighbours.random())
}
