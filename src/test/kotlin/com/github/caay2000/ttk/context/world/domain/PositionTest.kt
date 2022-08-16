package com.github.caay2000.ttk.context.world.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class PositionTest {

    @Test
    fun `distance between 0,0 and 0,1 is 1`() {
        val source = Position(0, 0)
        val destination = Position(0, 1)
        assertThat(source.distance(destination)).isEqualTo(1)
    }

    @Test
    fun `distance between 0,0 and 0,-1 is 1`() {
        val source = Position(0, 0)
        val destination = Position(0, -1)
        assertThat(source.distance(destination)).isEqualTo(1)
    }

    @Test
    fun `distance between 0,0 and 0,2 is 2`() {
        val source = Position(0, 0)
        val destination = Position(0, 2)
        assertThat(source.distance(destination)).isEqualTo(2)
    }

    @ParameterizedTest
    @MethodSource("1,1 neighbours")
    fun `distance between 1,1 and neighbours is 1`(destination: Position) {
        assertThat(Position(1, 1).distance(destination)).isEqualTo(1)
    }

    @ParameterizedTest
    @MethodSource("2,2 neighbours")
    fun `distance between 2,2 and neighbours is 1`(destination: Position) {
        assertThat(Position(2, 2).distance(destination)).isEqualTo(1)
    }

    companion object {
        @JvmStatic
        fun `1,1 neighbours`(): Stream<Arguments> {
            return Position(1, 1).neighbours.map {
                Arguments.of(it)
            }.stream()
        }

        @JvmStatic
        fun `2,2 neighbours`(): Stream<Arguments> {
            return Position(2, 2).neighbours.map {
                Arguments.of(it)
            }.stream()
        }
    }
}
