package com.github.caay2000.ttk.integration

import com.github.caay2000.ttk.application.Application
import com.github.caay2000.ttk.domain.world.WorldProvider
import com.github.caay2000.ttk.infra.provider.DefaultWorldProvider
import com.github.caay2000.ttk.mother.ConfigurationMother
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ApplicationIntegrationTest {

    private val configuration = ConfigurationMother.random(worldWidth = 4, worldHeight = 6)
    private val provider: WorldProvider = DefaultWorldProvider()

    @Test
    fun `asaas`() {

        val sut = Application(configuration, provider)

        assertThat(sut.invoke()).isEqualTo(4)
    }
}
