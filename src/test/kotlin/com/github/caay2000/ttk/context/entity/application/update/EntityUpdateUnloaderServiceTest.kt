// package com.github.caay2000.ttk.context.entity.application.update
//
// import com.github.caay2000.ttk.api.event.Event
// import com.github.caay2000.ttk.api.event.EventPublisher
// import com.github.caay2000.ttk.context.entity.domain.Entity
// import com.github.caay2000.ttk.context.entity.domain.EntityStatus
// import com.github.caay2000.ttk.context.entity.event.EntityUnloadedEvent
// import com.github.caay2000.ttk.context.world.domain.Position
// import com.github.caay2000.ttk.infra.provider.DefaultProvider
// import com.github.caay2000.ttk.mother.EntityMother
// import com.github.caay2000.ttk.mother.WorldMother
// import io.kotest.assertions.arrow.either.shouldBeRight
// import org.assertj.core.api.Assertions.assertThat
// import org.junit.jupiter.api.Test
// import org.mockito.kotlin.mock
// import org.mockito.kotlin.verify
//
// internal class EntityUpdateUnloaderServiceTest {
//
//    private val provider = DefaultProvider()
//    private val eventPublisher: EventPublisher<Event> = mock()
//    private val sut = EntityUpdateUnloaderService(provider, eventPublisher)
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
//        `world exists`(alreadyUnloadedEntity)
//
//        sut.invoke(alreadyUnloadedEntity).shouldBeRight {
//            assertThat(it).isEqualTo(alreadyUnloadedEntity)
//        }
//    }
//
//    @Test
//    fun `should unload passengers when reaches a station`() {
//
//        `world exists`(loadedEntity)
//
//        sut.invoke(loadedEntity).shouldBeRight {
//            assertThat(it.pax).isEqualTo(0)
//        }
//    }
//
//    @Test
//    fun `should publish an event when unloads passengers`() {
//
//        `world exists`(loadedEntity)
//
//        sut.invoke(loadedEntity).shouldBeRight {
//            verify(eventPublisher).publish(
//                listOf(EntityUnloadedEvent(loadedEntity.id, amount = 10, position = it.currentPosition))
//            )
//        }
//    }
//
//    @Test
//    fun `shouldn't publish an event when entity is empty`() {
//
//        `world exists`(unloadedEntity)
//
//        sut.invoke(unloadedEntity).shouldBeRight {
//            verify(eventPublisher).publish(emptyList())
//        }
//    }
//
//    private fun `world exists`(entity: Entity) {
//        provider.set(
//            WorldMother.random(
//                entities = mapOf(entity.id to entity)
//            )
//        )
//    }
//
//    private val loadedEntity = EntityMother.random(
//        currentPosition = Position(3, 0),
//        pax = 10,
//        status = EntityStatus.STOP
//    )
//    private val unloadedEntity = EntityMother.random(
//        currentPosition = Position(3, 0),
//        pax = 0,
//        status = EntityStatus.STOP
//    )
//    private val alreadyUnloadedEntity: Entity = EntityMother.random(
//        currentPosition = Position(3, 0),
//        currentDuration = 1,
//        status = EntityStatus.STOP
//    )
//    private val movingEntity: Entity = EntityMother.random(
//        status = EntityStatus.IN_ROUTE
//    )
// }
