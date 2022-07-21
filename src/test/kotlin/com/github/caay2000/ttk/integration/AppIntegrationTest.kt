package com.github.caay2000.ttk.integration

import com.github.caay2000.ttk.App
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppIntegrationTest {

    private val sut: App = App()

    @ParameterizedTest
    @CsvSource(value = ["0,0:3,2:4", "0,0:0,4:3"], delimiter = ':')
    @Disabled
    fun `test data 1`(src: String, dest: String, result: Int) {

        val result = sut.invoke("0,0", "3,2")

        assertThat(result).isEqualTo(4)
    }
}
