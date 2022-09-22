package com.github.caay2000.ttk.context.location.application

import arrow.core.right
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.location.application.LocationRepository.FindLocationCriteria.ByPosition
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

internal class LocationCargoLoaderServiceTest {

    private val locationRepository: LocationRepository = mock()
    private val eventPublisher: EventPublisher = mock()
    private val sut = LocationCargoLoaderService(locationRepository, eventPublisher)

    @Test
    fun `should remove the amount passengers from station`() {

        `location exists`()
        `location will be updated`()

        sut.invoke(location.position, 10).shouldBeRight {
            verify(locationRepository).save(it)
            assertThat(it.rawPAX).isEqualTo(10.0)
        }
    }

    private fun `location exists`() {
        whenever(locationRepository.find(ByPosition(location.position))).thenReturn(location.right())
    }

    private fun `location will be updated`() {
        whenever(locationRepository.save(any())).thenReturnFirstArgument<Location> { it.right() }
    }

    private val location = LocationMother.random(rawPAX = 20.0)
}
