// package com.github.caay2000.ttk.context.world.application
//
// import arrow.core.computations.ResultEffect.bind
// import com.github.caay2000.ttk.infra.provider.DefaultProvider
// import com.github.caay2000.ttk.mother.WorldMother
// import io.kotest.assertions.arrow.either.shouldBeRight
// import org.assertj.core.api.Assertions.assertThat
// import org.junit.jupiter.api.Test
// import org.mockito.kotlin.mock
//
// internal class WorldUpdaterServiceTest {
//
//    private val provider = DefaultProvider()
//    private val sut = WorldUpdaterService(provider, mock())
//
//    @Test
//    fun `turn is updated correctly`() {
//
//        `world exists`()
//
//        sut.invoke().shouldBeRight {
//            assertThat(it.currentTurn).isEqualTo(1)
//            assertThat(it).isEqualTo(provider.get().bind())
//        }
//    }
//
//    @Test
//    fun `entities are also updated`() {
//
//        `world exists`()
//
//        sut.invoke().shouldBeRight {
//            assertThat(it.currentTurn).isEqualTo(1)
//            assertThat(it).isEqualTo(provider.get().bind())
//        }
//    }
//
//    private fun `world exists`() = provider.set(WorldMother.empty(width = 3, height = 3))
// }