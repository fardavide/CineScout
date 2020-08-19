package client.cli.view

import assert4k.assert
import assert4k.equals
import assert4k.that
import client.cli.state.GetSuggestionState
import client.cli.util.CliTest
import domain.Test.Movie.Blow
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test

internal class SuggestionTest : CliTest() {

    @Test
    fun `SearchResult is displayed correctly`() = runBlockingTest {
        val resultState = Suggestion(Blow, GetSuggestionState.actions)
        val result = resultState.render()
        assert that result equals SUGGESTION_ACTION
    }

    private companion object {
        val SUGGESTION_ACTION = """
            |┌───────────────────────────┬───────────────────────────────────┬──────────┐
            |│   ID: 4133                │   Blow                            │   2001   │
            |├───────────────────────────┼───────────────────────────────────┴──────────┤
            |│   Cast                    │   Johnny Depp, Penélope Cruz, Ethan Suplee   │
            |├───────────────────────────┼──────────────────────────────────────────────┤
            |│   Genres                  │   Crime, Drama                               │
            |└───────────────────────────┴──────────────────────────────────────────────┘
            |                                                                            
            |┌──────────────────────────────────────────────────┐                        
            |│             Do you like this movie?              │                        
            |├───────────────────────────┬────────────┬─────────┤                        
            |│   Yes                     │   yes      │   y     │                        
            |├───────────────────────────┼────────────┼─────────┤                        
            |│   No                      │   no       │   n     │                        
            |├───────────────────────────┼────────────┼─────────┤                        
            |│   Skip                    │   skip     │   s     │                        
            |├───────────────────────────┼────────────┼─────────┤                        
            |│   Back to the main menu   │   *home    │   *h    │                        
            |└───────────────────────────┴────────────┴─────────┘                        
            |
        """.trimMargin()
    }
}
