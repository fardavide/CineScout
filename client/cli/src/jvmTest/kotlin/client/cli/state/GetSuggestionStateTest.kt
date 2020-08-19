package client.cli.state

import assert4k.`is`
import assert4k.assert
import assert4k.that
import assert4k.type
import client.cli.util.CliTest
import client.viewModel.GetSuggestedMovieViewModel
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test

internal class GetSuggestionStateTest : CliTest() {

    private val viewModel: GetSuggestedMovieViewModel = mockk(relaxed = true)
    private val state = GetSuggestionState(viewModel)

    @Test
    fun `Home is displayed, when command "*home" is inserted`() = runBlockingTest {
        val result = state execute "*home"
        assert that result `is` type<MenuState>()
    }

    @Test
    fun `Home is displayed, when command "*h" capitalized is inserted`() = runBlockingTest {
        val result = state execute "*h"
        assert that result `is` type<MenuState>()
    }

    @Test
    fun `Movie is Liked correctly by "yes"`() = runBlockingTest {
        state execute "yes"
        coVerify { viewModel.likeCurrent() }
    }

    @Test
    fun `Movie is Liked correctly by "y"`() = runBlockingTest {
        state execute "y"
        coVerify { viewModel.likeCurrent() }
    }

    @Test
    fun `Movie is Disliked correctly by "no"`() = runBlockingTest {
        state execute "no"
        coVerify { viewModel.dislikeCurrent() }
    }

    @Test
    fun `Movie is Disliked correctly by "n"`() = runBlockingTest {
        state execute "n"
        coVerify { viewModel.dislikeCurrent() }
    }

    @Test
    fun `Movie is Skipped correctly by "skip"`() = runBlockingTest {
        state execute "skip"
        coVerify { viewModel.skipCurrent() }
    }

    @Test
    fun `Movie is Skipped correctly by "s"`() = runBlockingTest {
        state execute "s"
        coVerify { viewModel.skipCurrent() }
    }
}
