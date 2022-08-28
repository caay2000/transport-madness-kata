package com.github.caay2000.ttk.context.world.application

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.context.world.secondary.InMemoryWorldRepository
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class WorldCreatorServiceTest {

    private val worldRepository: WorldRepository = InMemoryWorldRepository(InMemoryDatabase())
    private val eventPublisher: EventPublisher = mock()
    private val sut = WorldCreatorService(worldRepository, eventPublisher)

    @Test
    fun `world is created correctly`() {

        sut.invoke().shouldBeRight {
            assertThat(it).isEqualTo(worldRepository.get().bind())
        }
    }
}
