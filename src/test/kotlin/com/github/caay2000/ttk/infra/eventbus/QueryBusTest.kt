package com.github.caay2000.ttk.infra.eventbus

import com.github.caay2000.ttk.api.event.Command
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.Query
import com.github.caay2000.ttk.api.event.QueryResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

internal class QueryBusTest {

    @Test
    internal fun `subscribers receive the published event`() {

        KTEventBus.init<Command, Query, Event>()
        instantiateQueryHandler(TestQuery::class, TestQueryHandler())

        val queryTrue = TestQuery(value = true)
        val queryFalse = TestQuery(value = false)
        val resultTrue = KTQueryExecutor().execute<TestQuery, TestQueryResponse>(queryTrue)
        val resultFalse = KTQueryExecutor().execute<TestQuery, TestQueryResponse>(queryFalse)

        assertThat(resultTrue.value).isEqualTo("true")
        assertThat(resultTrue.queryId).isEqualTo(queryTrue.queryId)
        assertThat(resultFalse.value).isEqualTo("false")
        assertThat(resultFalse.queryId).isEqualTo(queryFalse.queryId)
    }

    inner class TestQueryHandler : KTQueryHandler<TestQuery, TestQueryResponse>(TestQuery::class) {
        override fun handle(query: TestQuery): TestQueryResponse {
            return TestQueryResponse(query.value.toString())
        }
    }

    internal data class TestQuery(val value: Boolean) : Query {
        override val queryId: UUID = UUID.randomUUID()
    }

    internal data class TestQueryResponse(override val value: String) : QueryResponse {
        override val queryId: UUID = UUID.randomUUID()
    }
}
