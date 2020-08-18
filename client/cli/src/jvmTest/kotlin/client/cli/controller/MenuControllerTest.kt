package client.cli.controller

import assert4k.*
import client.cli.state.GetSuggestionsState
import client.cli.state.RateMovieState
import client.cli.state.SearchState
import client.cli.util.CliTest
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test

internal class MenuControllerTest : CliTest {

    private val controller = MenuController()

    @Test
    fun `Search is displayed, when command "1" is inserted`() = runBlockingTest {
        val result = controller execute "1"
        assert that result `is` type<SearchState>()
    }

    @Test
    fun `Search is displayed, when command "Search" capitalized is inserted`() = runBlockingTest {
        val result = controller execute "Search"
        assert that result `is` type<SearchState>()
    }

    @Test
    fun `Search is displayed, when command "search" is inserted`() = runBlockingTest {
        val result = controller execute "search"
        assert that result `is` type<SearchState>()
    }


    @Test
    fun `Rate is displayed when command "2" is inserted`() = runBlockingTest {
        val result = controller execute "2"
        assert that result `is` type<RateMovieState>()
    }

    @Test
    fun `Rate is displayed when command "Rate" capitalized is inserted`() = runBlockingTest {
        val result = controller execute "Rate"
        assert that result `is` type<RateMovieState>()
    }

    @Test
    fun `Rate is displayed when command "rate" is inserted`() = runBlockingTest {
        val result = controller execute "rate"
        assert that result `is` type<RateMovieState>()
    }


    @Test
    fun `GetSuggestion is displayed when command "3" is inserted`() = runBlockingTest {
        val result = controller execute "3"
        assert that result `is` type<GetSuggestionsState>()
    }

    @Test
    fun `GetSuggestion is displayed when command "Suggestion" capitalized is inserted`() = runBlockingTest {
        val result = controller execute "Suggestion"
        assert that result `is` type<GetSuggestionsState>()
    }

    @Test
    fun `GetSuggestion is displayed when command "suggestion" is inserted`() = runBlockingTest {
        val result = controller execute "suggestion"
        assert that result `is` type<GetSuggestionsState>()
    }
}
