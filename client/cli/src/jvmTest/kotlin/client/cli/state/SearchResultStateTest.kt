package client.cli.state

import assert4k.*
import client.cli.Cli_Test.Companion.RENDERED_MOVIE_BLOW
import client.cli.Cli_Test.Companion.RENDERED_MOVIE_PUL_FICTION
import client.cli.util.CliTest
import domain.Test.Movie.Blow
import domain.Test.Movie.PulpFiction
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test

internal class SearchResultStateTest : CliTest {

    @Test
    fun `Search is displayed correctly`() = runBlockingTest {
        val resultState = SearchResultState(setOf(Blow, PulpFiction))
        val result = resultState.render()
        assert that result equals RENDERED_MOVIE_BLOW + RENDERED_MOVIE_PUL_FICTION + HOME_ACTION
    }

    private companion object {
        val HOME_ACTION = """
            |┌─────────────────────────────┬─────────────┬─────────┐                   
            |│   Back to the main menu     │   *home     │   *h    │                   
            |└─────────────────────────────┴─────────────┴─────────┘                   
            |
        """.trimMargin()
    }
}
