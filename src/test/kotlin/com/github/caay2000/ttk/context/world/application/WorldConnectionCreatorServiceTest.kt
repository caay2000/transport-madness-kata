package com.github.caay2000.ttk.context.world.application

import arrow.core.right
import com.github.caay2000.ttk.api.event.QueryExecutor
import com.github.caay2000.ttk.context.company.primary.query.FindCompanyQuery
import com.github.caay2000.ttk.context.company.primary.query.FindCompanyQueryResponse
import com.github.caay2000.ttk.context.pathfinding.primary.query.FindPathQuery
import com.github.caay2000.ttk.context.pathfinding.primary.query.FindPathQueryResponse
import com.github.caay2000.ttk.context.world.domain.Cell
import com.github.caay2000.ttk.context.world.domain.CompanyNotFoundException
import com.github.caay2000.ttk.context.world.domain.Position
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.extension.thenReturnFirstArgument
import com.github.caay2000.ttk.mother.CompanyMother
import com.github.caay2000.ttk.mother.WorldMother
import io.kotest.assertions.arrow.either.shouldBeLeftOfType
import io.kotest.assertions.arrow.either.shouldBeRight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class WorldConnectionCreatorServiceTest {

    private val worldRepository: WorldRepository = mock()
    private val queryExecutor: QueryExecutor = mock()

    private val sut = WorldConnectionCreatorService(
        worldRepository = worldRepository,
        queryExecutor = queryExecutor,
        eventPublisher = mock()
    )

    @Test
    fun `should create connection when it does not exists`() {

        `world exists`()
        `company exists`()
        `pathfinding returns correct connection`()
        `world will be saved`()

        sut.invoke(company.id, Position(0, 0), Position(3, 2)).shouldBeRight {
            assertThat(it.cells.values.filter { cell -> cell.connected }.toSet()).isEqualTo(expectedPath)
            verify(worldRepository).save(it)
        }
    }

    @Test
    fun `should throw error when company does not exists`() {

        `company does not exists`()

        sut.invoke(company.id, Position(0, 0), Position(3, 2)).shouldBeLeftOfType<CompanyNotFoundException>()
    }

    private fun `company exists`() =
        whenever(queryExecutor.execute<FindCompanyQueryResponse>(any<FindCompanyQuery>()))
            .thenReturn(FindCompanyQueryResponse(company))

    private fun `company does not exists`() =
        whenever(queryExecutor.execute<FindCompanyQueryResponse>(any<FindCompanyQuery>()))
            .thenThrow(RuntimeException("not exists"))

    private fun `world exists`() = whenever(worldRepository.get()).thenReturn(WorldMother.empty().right())

    private fun `pathfinding returns correct connection`() =
        whenever(queryExecutor.execute<FindPathQueryResponse>(any<FindPathQuery>()))
            .thenReturn(FindPathQueryResponse(expectedPath.toList()))

    private fun `world will be saved`() {
        whenever(worldRepository.save(any())).thenReturnFirstArgument<World> { it.right() }
    }

    private val company = CompanyMother.random()

    private val expectedPath = setOf(
        Cell(position = Position(x = 0, y = 0), connection = Cell.ConnectedCell(company.id)),
        Cell(position = Position(x = 1, y = 0), connection = Cell.ConnectedCell(company.id)),
        Cell(position = Position(x = 2, y = 0), connection = Cell.ConnectedCell(company.id)),
        Cell(position = Position(x = 2, y = 1), connection = Cell.ConnectedCell(company.id)),
        Cell(position = Position(x = 3, y = 2), connection = Cell.ConnectedCell(company.id))
    )
}
