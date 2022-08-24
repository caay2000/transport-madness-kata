// package com.github.caay2000.ttk.context.location.application
//
// import arrow.core.computations.ResultEffect.bind
// import com.github.caay2000.ttk.api.provider.Provider
// import com.github.caay2000.ttk.context.location.domain.LocationsTooCloseException
// import com.github.caay2000.ttk.context.world.domain.Position
// import com.github.caay2000.ttk.infra.provider.DefaultProvider
// import com.github.caay2000.ttk.mother.ConfigurationMother
// import com.github.caay2000.ttk.mother.WorldMother
// import com.github.caay2000.ttk.mother.world.location.PopulationMother
// import io.kotest.assertions.arrow.either.shouldBeLeftOfType
// import io.kotest.assertions.arrow.either.shouldBeRight
// import org.assertj.core.api.Assertions.assertThat
// import org.junit.jupiter.api.Test
// import org.mockito.kotlin.mock
//
// class LocationCreatorServiceTest {
//
//    private val configuration = ConfigurationMother.random()
//    private val provider: Provider = DefaultProvider()
//    private val sut = LocationCreatorService(provider, mock())
//
//    @Test
//    fun `city is created correctly`() {
//
//        provider.set(WorldMother.empty())
//        provider.setConfiguration(configuration)
//
//        val name = "A"
//        val position = Position(0, 0)
//        val population = PopulationMother.random()
//
//        sut.invoke(name, position, population).shouldBeRight {
//            assertThat(it.locations).hasSize(1)
//            val location = it.locations.values.first()
//            assertThat(location.position).isEqualTo(position)
//            assertThat(location.population).isEqualTo(population)
//            assertThat(it).isEqualTo(provider.get().bind())
//        }
//    }
//
//    @Test
//    fun `city creation fails if distance between cities is less than configuration$minDistanceBetweenCities`() {
//        provider.set(WorldMother.empty())
//        provider.setConfiguration(configuration)
//
//        sut.invoke("A", Position(0, 0), PopulationMother.random()).shouldBeRight()
//        sut.invoke("B", Position(0, 2), PopulationMother.random()).shouldBeLeftOfType<LocationsTooCloseException>()
//    }
// }
