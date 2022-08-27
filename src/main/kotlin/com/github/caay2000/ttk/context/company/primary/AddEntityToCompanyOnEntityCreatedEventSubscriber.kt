package com.github.caay2000.ttk.context.company.primary

import arrow.core.computations.ResultEffect.bind
import com.github.caay2000.ttk.api.event.Event
import com.github.caay2000.ttk.api.event.EventPublisher
import com.github.caay2000.ttk.api.event.EventSubscriber
import com.github.caay2000.ttk.context.company.application.CompanyEntityAdderService
import com.github.caay2000.ttk.context.company.application.CompanyRepository
import com.github.caay2000.ttk.context.entity.event.EntityCreatedEvent

class AddEntityToCompanyOnEntityCreatedEventSubscriber(companyRepository: CompanyRepository, eventPublisher: EventPublisher<Event>) : EventSubscriber<EntityCreatedEvent> {

    private val locationCargoLoaderService = CompanyEntityAdderService(companyRepository, eventPublisher)

    override fun handle(event: EntityCreatedEvent) =
        locationCargoLoaderService.invoke(event.companyId, event.aggregateId).void().bind()
}
