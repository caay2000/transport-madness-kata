package com.github.caay2000.ttk.context.world.secondary

import com.github.caay2000.ttk.context.world.application.WorldRepository
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeLeftOfType
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class InMemoryWorldRepositoryTest {

    private val db: InMemoryDatabase = InMemoryDatabase()
    private val sut: WorldRepository = InMemoryWorldRepository(db)

    @Test
    fun `get works`() {

        `world exists`()

        sut.get().shouldBeRight {
            Assertions.assertThat(it).isEqualTo(world)
        }
    }

    @Test
    fun `find returns NoSuchElementException if element does not exists`() {

        sut.get().shouldBeLeftOfType<NoSuchElementException>()
    }

    @Test
    fun `save updates existing element`() {

        `world exists`()

        sut.save(world.copy(currentTurn = world.currentTurn + 10))

        sut.get().shouldBeRight {
            Assertions.assertThat(it).isEqualTo(world.copy(currentTurn = world.currentTurn + 10))
        }
    }

    private fun `world exists`() {
        sut.save(world)
    }

    private val world = WorldMother.random()
}
