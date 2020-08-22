package client.cli

import assert4k.*
import client.cli.state.MenuState
import client.cli.util.CliTest
import client.cli.util.StringOutputStream
import client.cli.util.TestDispatchersProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import java.io.PrintStream
import kotlin.test.Test

@Suppress("ClassName")
internal class Cli_Test : CliTest() {

    private fun CoroutineScope.Cli() = Cli(this, TestDispatchersProvider())

    @Before
    fun setup() {
        theme = theme.copy(colorized = false)
    }

    @Test
    fun `Menu is the first State on start`() = runBlockingTest {
        val cli = Cli()
        assert that cli.state `is` type<MenuState>()
        cli.clear()
    }

    @Test
    fun `Menu is displayed correctly on start`() = runBlockingTest {
        val stringStream = StringOutputStream()
        System.setOut(PrintStream(stringStream))

        val cli = Cli()
        assert that stringStream.output equals RENDERED_MENU
        cli.clear()
    }

    @Test
    fun `Search is displayed correctly`() = runBlockingTest {
        val stringStream = StringOutputStream()

        val cli = Cli()
        System.setOut(PrintStream(stringStream))
        cli execute "search"
        assert that stringStream.output equals RENDERED_SEARCH
        cli.clear()
    }

    @Test
    fun `RateMovie is displayed correctly`() = runBlockingTest {
        val stringStream = StringOutputStream()

        val cli = Cli()
        System.setOut(PrintStream(stringStream))
        cli execute "rate"
        assert that stringStream.output equals RENDERED_RATE
        cli.clear()
    }

    @Test
    fun `Errors can be displayed correctly`() = runBlockingTest {
        val stringStream = StringOutputStream()
        System.setErr(PrintStream(stringStream))

        val cli = Cli()
        val command = "wrong command"
        cli execute command
        assert that stringStream.output equals "Cannot parse command '$command'"
        cli.clear()
    }

    internal companion object {
        val RENDERED_MENU = """
            ┌───────────────────────────────────────────────────────────────────┐
            │                                                                   │
            │                       Welcome to CineScout!                       │
            │                                                                   │
            ├──────────────────────────────────┬────────────────────────────────┤
            │                                  │            commands            │
            ├──────────────────────────────────┼───────┬────────────────┬───────┤
            │   Search a Movie by title        │   1   │   search       │   s   │
            ├──────────────────────────────────┼───────┼────────────────┼───────┤
            │   Rate a Movie by id             │   2   │   rate         │   r   │
            ├──────────────────────────────────┼───────┼────────────────┼───────┤
            │   Get suggested Movies for you   │   3   │   suggestion   │   g   │
            └──────────────────────────────────┴───────┴────────────────┴───────┘
            command:
        """.trimIndent()

        val RENDERED_SEARCH = """
            ┌──────────────────────────────────────────────────────────────────────────────┐
            │                                                                              │
            │     Insert the tile or part of it, for the Movie that you want to search     │
            │                                                                              │
            ├─────────────────────────────────────┬─────────────────────┬──────────────────┤
            │   Back to the main menu             │   *home             │   *h             │
            └─────────────────────────────────────┴─────────────────────┴──────────────────┘
            command:
        """.trimIndent()

        val RENDERED_MOVIE_BLOW = """
            |┌────────────────────┬────────────────────────────────────────┬──────────┐
            |│   ID: 4133         │   Blow                                 │   2001   │
            |├────────────────────┼────────────────────────────────────────┴──────────┤
            |│   Cast             │   Johnny Depp, Penélope Cruz, Ethan Suplee        │
            |├────────────────────┼───────────────────────────────────────────────────┤
            |│   Genres           │   Crime, Drama                                    │
            |└────────────────────┴───────────────────────────────────────────────────┘
            |                                                                          ${"\n"}
        """.trimMargin()

        val RENDERED_MOVIE_PUL_FICTION = """
            |┌────────────────────┬────────────────────────────────────────┬──────────┐
            |│   ID: 680          │   Pulp Fiction                         │   1994   │
            |├────────────────────┼────────────────────────────────────────┴──────────┤
            |│   Cast             │   John Travolta, Samuel L. Jackson, Uma Thurman   │
            |├────────────────────┼───────────────────────────────────────────────────┤
            |│   Genres           │   Crime, Thriller                                 │
            |└────────────────────┴───────────────────────────────────────────────────┘
            |                                                                          ${"\n"}
        """.trimMargin()

        val RENDERED_RATE = """
            ┌──────────────────────────────────────────────────────────────────────────┐
            │                                                                          │
            │     Insert the TMDB id of the Movie that you want to rate positively     │
            │                                                                          │
            ├────────────────────────────────────┬────────────────────┬────────────────┤
            │   Back to the main menu            │   *home            │   *h           │
            └────────────────────────────────────┴────────────────────┴────────────────┘
            command:
        """.trimIndent()

        val RENDERED_SUGGESTION = RENDERED_MOVIE_BLOW + """
            ┌────────────────────────────────────────────────┐
            │                                                │
            │            Do you like this movie?             │
            │                                                │
            ├───────────────────────────┬───────────┬────────┤
            │   Yes                     │   yes     │   y    │
            ├───────────────────────────┼───────────┼────────┤
            │   No                      │   no      │   n    │
            ├───────────────────────────┼───────────┼────────┤
            │   Skip                    │   skip    │   s    │
            ├───────────────────────────┼───────────┼────────┤
            │   Back to the main menu   │   *home   │   *h   │
            └───────────────────────────┴───────────┴────────┘
            command:
        """.trimIndent()
    }
}
