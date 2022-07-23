package com.github.caay2000.ttk.application.entity

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.domain.world.Position
import com.github.caay2000.ttk.infra.provider.DefaultWorldProvider
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeLeftOfType
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class EntityCreatorServiceTest {

    private val provider = DefaultWorldProvider()
    private val sut = EntityCreatorService(provider)

    @Test
    fun `entity is added to world correctly`() {

        provider.set(WorldMother.empty())

        sut.invoke(Position(1, 1)).shouldBeRight {
            assertThat(it.entities).hasSize(1)
            assertThat(it).isEqualTo(provider.get().bind())
        }
    }

    @ParameterizedTest
    @CsvSource(value = ["-1,0", "0,-1", "1,0", "0,1"])
    fun `entity added out of bounds throw exception`(x: Int, y: Int) {
        provider.set(WorldMother.empty(1, 1))

        sut.invoke(Position(x, y)).shouldBeLeftOfType<InvalidEntityPositionException>()
    }
}
