package com.github.caay2000.ttk.application.entity.movement

import com.github.caay2000.ttk.domain.world.Position
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class SimpleMovementStrategyTest {

    private val sut = SimpleMovementStrategy()

    @Test
    fun `movement from 0,0 to 1,0 is correct`() {
        val source = Position(0, 0)
        val destination = Position(0, 1)

        val result = sut.move(source, destination)

        Assertions.assertThat(result).isEqualTo(destination)
    }

    @Test
    fun `movement from 0,0 to 2,0 is correct`() {
        val source = Position(0, 0)
        val destination = Position(0, 2)

        val result = sut.move(source, destination)

        Assertions.assertThat(result).isEqualTo(Position(0, 1))
    }

    @Test
    fun `movement from 0,0 to 2,2 is correct`() {
        val source = Position(0, 0)
        val destination = Position(2, 2)

        val result = sut.move(source, destination)

        Assertions.assertThat(result).isEqualTo(Position(1, 0))
    }
}
