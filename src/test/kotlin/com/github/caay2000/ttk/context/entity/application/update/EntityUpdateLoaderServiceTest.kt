// package com.github.caay2000.ttk.context.entity.application.update
//
// import com.github.caay2000.ttk.api.event.Event
// import com.github.caay2000.ttk.api.event.EventPublisher
// import com.github.caay2000.ttk.context.entity.domain.Entity
// import com.github.caay2000.ttk.context.entity.domain.EntityStatus
// import com.github.caay2000.ttk.context.entity.event.EntityLoadedEvent
// import com.github.caay2000.ttk.context.location.domain.Location
// import com.github.caay2000.ttk.context.world.domain.Position
// import com.github.caay2000.ttk.infra.provider.DefaultProvider
// import com.github.caay2000.ttk.mother.ConfigurationMother
// import com.github.caay2000.ttk.mother.EntityMother
// import com.github.caay2000.ttk.mother.WorldMother
// import com.github.caay2000.ttk.mother.world.location.LocationMother
// import io.kotest.assertions.arrow.either.shouldBeRight
// import org.assertj.core.api.Assertions.assertThat
// import org.junit.jupiter.api.Test
// import org.mockito.kotlin.mock
// import org.mockito.kotlin.verify
//
// internal class EntityUpdateLoaderServiceTest {
//
//    private val provider = DefaultProvider()
//    private val eventPublisher: EventPublisher<Event> = mock()
//    private val sut = EntityUpdateLoaderService(provider, eventPublisher)
//
//    @Test
//    fun `should do nothing if entity is IN_ROUTE`() {
//
//        `world exists`(movingEntity)
//
//        sut.invoke(movingEntity).shouldBeRight {
//            assertThat(it).isEqualTo(movingEntity)
//        }
//    }
//
//    @Test
//    fun `should not load passengers when in station for less than 1 turn`() {
//
//        `world exists`(notReadyEntity)
//
//        sut.invoke(notReadyEntity).shouldBeRight {
//            assertThat(it).isEqualTo(notReadyEntity)
//        }
//    }
//
//    @Test
//    fun `should load passengers when in station for more than 1 turn`() {
//
//        `world exists`(readyToLoadEntity, crowdedLocation)
//
//        sut.invoke(readyToLoadEntity).shouldBeRight {
//            assertThat(it.pax).isEqualTo(30)
//        }
//    }
//
//    @Test
//    fun `should not load passengers if entity is full`() {
//
//        val fullEntity = readyToLoadEntity.copy(
//            pax = readyToLoadEntity.entityType.passengerCapacity
//        )
//
//        `world exists`(fullEntity, crowdedLocation)
//
//        sut.invoke(fullEntity).shouldBeRight {
//            assertThat(it).isEqualTo(fullEntity)
//            verify(eventPublisher).publish(emptyList())
//        }
//    }
//
//    @Test
//    fun `should load passengers only until full capacity of entity`() {
//
//        val almostFullEntity = readyToLoadEntity.copy(
//            pax = readyToLoadEntity.entityType.passengerCapacity - 1
//        )
//        `world exists`(almostFullEntity, crowdedLocation)
//
//        sut.invoke(almostFullEntity).shouldBeRight {
//            assertThat(it.pax).isEqualTo(readyToLoadEntity.entityType.passengerCapacity)
//            listOf(EntityLoadedEvent(aggregateId = readyToLoadEntity.id, amount = 1, position = Position(3, 0)))
//        }
//    }
//
//    @Test
//    fun `should publish EntityLoadedEvent when passengers are loaded`() {
//
//        `world exists`(readyToLoadEntity, crowdedLocation)
//
//        sut.invoke(readyToLoadEntity).shouldBeRight {
//            verify(eventPublisher).publish(
//                listOf(EntityLoadedEvent(aggregateId = readyToLoadEntity.id, amount = 20, position = Position(3, 0)))
//            )
//        }
//    }
//
//    @Test
//    fun `shouldn't publish event when location is empty`() {
//
//        `world exists`(readyToLoadEntity, emptyLocation)
//
//        sut.invoke(readyToLoadEntity).shouldBeRight {
//            verify(eventPublisher).publish(emptyList())
//        }
//    }
//
//    private fun `world exists`(entity: Entity = readyToLoadEntity, location: Location = crowdedLocation) {
//        provider.set(
//            WorldMother.random(
//                entities = mapOf(entity.id to entity),
//                locations = mapOf(location.id to location)
//            )
//        )
//        provider.setConfiguration(ConfigurationMother.random())
//    }
//
//    private val readyToLoadEntity: Entity = EntityMother.random(
//        currentPosition = Position(3, 0),
//        currentDuration = 1,
//        pax = 10,
//        status = EntityStatus.STOP
//    )
//    private val notReadyEntity: Entity = EntityMother.random(
//        currentDuration = 0,
//        status = EntityStatus.STOP
//    )
//    private val movingEntity: Entity = EntityMother.random(
//        status = EntityStatus.IN_ROUTE
//    )
//    private val crowdedLocation: Location = LocationMother.random(position = Position(3, 0), rawPAX = 20.0)
//    private val emptyLocation: Location = LocationMother.random(position = Position(3, 0), rawPAX = 0.0)
// }
