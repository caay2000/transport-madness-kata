package com.github.caay2000.ttk.domain.world

import com.github.caay2000.ttk.context.world.domain.Position
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PositionTest {

    @Test
    fun `distance between 0,0 and 0,1 is 1`() {
        val source = Position(0, 0)
        val destination = Position(0, 1)
        assertThat(source.distance(destination)).isEqualTo(1F)
    }

    @Test
    fun `distance between 0,0 and 0,-1 is 1`() {
        val source = Position(0, 0)
        val destination = Position(0, -1)
        assertThat(source.distance(destination)).isEqualTo(1F)
    }

    @Test
    fun `distance between 0,0 and 0,2 is 2`() {
        val source = Position(0, 0)
        val destination = Position(0, 2)
        assertThat(source.distance(destination)).isEqualTo(2F)
    }
}
