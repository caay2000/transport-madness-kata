package com.github.caay2000.ttk.context.entity.secondary

import com.github.caay2000.ttk.context.entity.application.EntityRepository
import com.github.caay2000.ttk.infra.database.InMemoryDatabase
import com.github.caay2000.ttk.mother.EntityMother
import io.kotest.assertions.arrow.either.shouldBeLeftOfType
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class InMemoryEntityRepositoryTest {

    private val db: InMemoryDatabase = InMemoryDatabase()
    private val sut: EntityRepository = InMemoryEntityRepository(db)

    @Test
    fun `exists returns true if it exist`() {

        `entity exists`()

        val result = sut.exists(EntityRepository.FindEntityCriteria.ByIdCriteria(entity.id))

        Assertions.assertThat(result).isTrue
    }

    @Test
    fun `exists returns false if it does not exist`() {

        val result = sut.exists(EntityRepository.FindEntityCriteria.ByIdCriteria(nonExistingEntity.id))

        Assertions.assertThat(result).isFalse
    }

    @Test
    fun `find ById works`() {

        `entity exists`()

        sut.find(EntityRepository.FindEntityCriteria.ByIdCriteria(entity.id)).shouldBeRight {
            Assertions.assertThat(it).isEqualTo(entity)
        }
    }

    @Test
    fun `find returns NoSuchElementException if element does not exists`() {

        sut.find(EntityRepository.FindEntityCriteria.ByIdCriteria(nonExistingEntity.id)).shouldBeLeftOfType<NoSuchElementException>()
    }

    @Test
    fun `findAll returns all items`() {

        `3 entities exists`()

        sut.findAll().shouldBeRight {
            Assertions.assertThat(it).hasSize(3)
        }
    }

    @Test
    fun `save updates existing element`() {

        `entity exists`()

        sut.save(entity.copy(pax = entity.pax + 10))

        sut.find(EntityRepository.FindEntityCriteria.ByIdCriteria(entity.id)).shouldBeRight {
            Assertions.assertThat(it).isEqualTo(entity.copy(pax = entity.pax + 10))
        }
    }

    private fun `entity exists`() {
        sut.save(entity)
    }

    private fun `3 entities exists`() {
        sut.save(entity)
        sut.save(EntityMother.random())
        sut.save(EntityMother.random())
    }

    private val entity = EntityMother.random()
    private val nonExistingEntity = EntityMother.random()
}
