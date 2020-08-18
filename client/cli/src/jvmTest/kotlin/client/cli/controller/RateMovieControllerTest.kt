package client.cli.controller

import assert4k.*
import client.cli.state.MenuState
import client.cli.util.CliTest
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test

internal class RateMovieControllerTest : CliTest {

    private val controller = RateMovieController()

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
