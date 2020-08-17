package client.cli

import assert4k.*
import client.cli.util.TestDispatchersProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test

internal class MenuTest {

    private fun CoroutineScope.Cli() = Cli(this, TestDispatchersProvider())

    @Test
    fun `Search is displayed, when command "1" is inserted`() = runBlockingTest {
        val cli = Cli()
        cli execute "Search"
        assert that cli.state equals State.Search
        cli.clear()
    }

    @Test
    fun `Search is displayed, when command "Search" capitalized is inserted`() = runBlockingTest {
        val cli = Cli()
        cli execute "Search"
        assert that cli.state equals State.Search
        cli.clear()
    }

    @Test
    fun `Search is displayed, when command "search" is inserted`() = runBlockingTest {
        val cli = Cli()
        cli execute "search"
        assert that cli.state equals State.Search
        cli.clear()
    }


    @Test
    fun `Rate is displayed when command "2" is inserted`() = runBlockingTest {
        val cli = Cli()
        cli execute "Rate"
        assert that cli.state equals State.Rate
        cli.clear()
    }

    @Test
    fun `Rate is displayed when command "Rate" capitalized is inserted`() = runBlockingTest {
        val cli = Cli()
        cli execute "Rate"
        assert that cli.state equals State.Rate
        cli.clear()
    }

    @Test
    fun `Rate is displayed when command "rate" is inserted`() = runBlockingTest {
        val cli = Cli()
        cli execute "rate"
        assert that cli.state equals State.Rate
        cli.clear()
    }


    @Test
    fun `GetSuggestion is displayed when command "3" is inserted`() = runBlockingTest {
        val cli = Cli()
        cli execute "Suggestion"
        assert that cli.state equals State.GetSuggestions
        cli.clear()
    }

    @Test
    fun `GetSuggestion is displayed when command "Suggestion" capitalized is inserted`() = runBlockingTest {
        val cli = Cli()
        cli execute "Suggestion"
        assert that cli.state equals State.GetSuggestions
        cli.clear()
    }

    @Test
    fun `GetSuggestion is displayed when command "suggestion" is inserted`() = runBlockingTest {
        val cli = Cli()
        cli execute "suggestion"
        assert that cli.state equals State.GetSuggestions
        cli.clear()
    }
}
