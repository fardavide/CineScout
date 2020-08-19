package client.cli.state

import client.cli.Action
import client.cli.error.throwWrongCommand
import client.cli.view.Menu
import org.koin.core.get

object MenuState : State() {

    private val SearchAction = Action("Search a Movie by title", "1", "search", "s")
    private val RateMovieAction = Action("Rate a Movie by id", "2", "rate", "r")
    private val GetSuggestionsAction = Action("Get suggested Movies for you", "3", "suggestion", "g")

    override val actions = setOf(
        SearchAction,
        RateMovieAction,
        GetSuggestionsAction
    )

    override suspend infix fun execute(command: String): State {
        return when (actionBy(command)) {
            SearchAction -> SearchState(searchViewModel = get())
            RateMovieAction -> RateMovieState(rateMovieViewModel = get())
            GetSuggestionsAction -> GetSuggestionState(getSuggestedMovieViewModel = get())
            else -> command.throwWrongCommand()
        }
    }

    override fun render() = Menu(actions).render()
}
