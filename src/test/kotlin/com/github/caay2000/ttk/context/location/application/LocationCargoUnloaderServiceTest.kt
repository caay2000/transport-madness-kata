package com.github.caay2000.ttk.context.location.application

import arrow.core.right
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.location.domain.Location
import com.github.caay2000.ttk.extension.thenReturnFirstArgument
import com.github.caay2000.ttk.mother.world.location.LocationMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class LocationCargoUnloaderServiceTest {

    private val locationRepository: LocationRepository = mock()
    private val eventPublisher: EventPublisher = mock()
    private val sut = LocationCargoUnloaderService(locationRepository, eventPublisher)

    @Test
    fun `should remove the amount passengers from station`() {

        `location exists`()
        `location will be updated`()

        val amountUnloaded = 10
        sut.invoke(location.position, amountUnloaded).shouldBeRight {
            verify(locationRepository).save(it)
            assertThat(it.received).isEqualTo(amountUnloaded)
        }
    }

    private fun `location exists`() {
        whenever(locationRepository.find(LocationRepository.FindLocationCriteria.ByPositionCriteria(location.position))).thenReturn(location.right())
    }

    private fun `location will be updated`() {
        whenever(locationRepository.save(any())).thenReturnFirstArgument<Location> { it.right() }
    }

    private val location = LocationMother.random(rawPAX = 20.0)
}
