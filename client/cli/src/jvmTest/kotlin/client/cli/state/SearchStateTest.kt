package client.cli.state

import assert4k.`is`
import assert4k.assert
import assert4k.that
import assert4k.type
import client.cli.util.CliTest
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test

internal class SearchStateTest : CliTest() {

    private val controller = SearchState(mockk())

    @Test
    fun `Home is displayed, when command "*home" is inserted`() = runBlockingTest {
        val result = controller execute "*home"
        assert that result `is` type<MenuState>()
    }

    @Test
    fun `Home is displayed, when command "*h" capitalized is inserted`() = runBlockingTest {
        val result = controller execute "*h"
        assert that result `is` type<MenuState>()
    }
}
