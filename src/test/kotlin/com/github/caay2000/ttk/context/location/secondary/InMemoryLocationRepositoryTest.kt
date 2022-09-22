package com.github.caay2000.ttk.context.location.secondary

import com.github.caay2000.ttk.context.location.application.LocationRepository
import com.github.caay2000.ttk.context.location.application.LocationRepository.FindLocationCriteria.ById
import com.github.caay2000.ttk.context.location.application.LocationRepository.FindLocationCriteria.ByPosition
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import com.github.caay2000.ttk.mother.world.location.LocationMother
import io.kotest.assertions.arrow.either.shouldBeLeftOfType
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class InMemoryLocationRepositoryTest {

    private val db: InMemoryDatabase = InMemoryDatabase()
    private val sut: LocationRepository = InMemoryLocationRepository(db)

    @Test
    fun `exists returns true if it exist`() {

        `location exists`()

        val result = sut.exists(ById(location.id))

        assertThat(result).isTrue
    }

    @Test
    fun `exists returns false if it does not exist ById`() {

        val result = sut.exists(ById(nonExistingLocation.id))

        assertThat(result).isFalse
    }

    @Test
    fun `exists returns false if it does not exist ByPosition`() {

        val result = sut.exists(ByPosition(nonExistingLocation.position))

        assertThat(result).isFalse
    }

    @Test
    fun `find ById works`() {

        `location exists`()

        sut.find(ById(location.id)).shouldBeRight {
            assertThat(it).isEqualTo(location)
        }
    }

    @Test
    fun `find ByPosition works`() {

        `location exists`()

        sut.find(ByPosition(location.position)).shouldBeRight {
            assertThat(it).isEqualTo(location)
        }
    }

    @Test
    fun `find returns NoSuchElementException if element does not exists`() {

        sut.find(ById(nonExistingLocation.id)).shouldBeLeftOfType<NoSuchElementException>()
    }

    @Test
    fun `findAll returns all items`() {

        `3 locations exists`()

        sut.findAll().shouldBeRight {
            assertThat(it).hasSize(3)
        }
    }

    @Test
    fun `save updates existing element`() {

        `location exists`()

        sut.save(location.copy(name = "newName"))

        sut.find(ById(location.id)).shouldBeRight {
            assertThat(it).isEqualTo(location.copy(name = "newName"))
        }
    }

    private fun `location exists`() {
        sut.save(location)
    }

    private fun `3 locations exists`() {
        sut.save(location)
        sut.save(LocationMother.random())
        sut.save(LocationMother.random())
    }

    private val location = LocationMother.random()
    private val nonExistingLocation = LocationMother.random()
}
