package com.github.caay2000.ttk.application.world

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.infra.provider.DefaultProvider
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class WorldUpdaterServiceTest {

    private val provider = DefaultProvider()
    private val sut = WorldUpdaterService(provider)

    @Test
    fun `turn is updated correctly`() {

        provider.set(WorldMother.empty(3, 3))

        sut.invoke().shouldBeRight {
            assertThat(it.currentTurn).isEqualTo(1)
            assertThat(it).isEqualTo(provider.get().bind())
        }
    }
}
