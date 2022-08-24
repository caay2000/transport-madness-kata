// package com.github.caay2000.ttk.context.location.application
//
// import arrow.core.computations.ResultEffect.bind
// import com.github.caay2000.ttk.infra.provider.DefaultProvider
// import com.github.caay2000.ttk.mother.WorldMother
// import com.github.caay2000.ttk.mother.world.location.LocationMother
// import io.kotest.assertions.arrow.either.shouldBeRight
// import org.assertj.core.api.Assertions.assertThat
// import org.junit.jupiter.api.Test
// import org.mockito.kotlin.mock
//
// internal class LocationCargoUnloaderServiceTest {
//
//    private val provider = DefaultProvider()
//    private val sut = LocationCargoUnloaderService(provider, mock())
//
//    @Test
//    fun `should remove the amount passengers from station`() {
//
//        val world = WorldMother.oneLocation(LocationMother.random(rawPAX = 20.0))
//        provider.set(world)
//        val location = world.locations.values.first()
//
//        sut.invoke(location.position, 10).shouldBeRight {
//            assertThat(it.received).isEqualTo(10)
//            assertThat(it).isEqualTo(provider.get().bind().getLocation(location.id))
//        }
//    }
// }
