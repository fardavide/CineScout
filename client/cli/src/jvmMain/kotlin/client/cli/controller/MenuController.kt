package client.cli.controller

import client.cli.Action
import client.cli.error.throwWrongCommand
import client.cli.state.AnyState
import client.cli.state.GetSuggestionsState
import client.cli.state.RateMovieState
import client.cli.state.SearchState

class MenuController : Controller() {

    private val SearchAction = Action("Search a Movie by title", "1", "search", "s")
    private val RateMovieAction = Action("Rate a Movie by id", "2", "rate", "r")
    private val GetSuggestionsAction = Action("Get suggested Movies for you", "3", "suggestion", "g")

    override val actions = setOf(
        SearchAction,
        RateMovieAction,
        GetSuggestionsAction
    )

    override suspend infix fun execute(command: String): AnyState {
        return when (actionBy(command)) {
            SearchAction -> SearchState()
            RateMovieAction -> RateMovieState()
            GetSuggestionsAction -> GetSuggestionsState()
            else -> command.throwWrongCommand()
        }
    }
}
