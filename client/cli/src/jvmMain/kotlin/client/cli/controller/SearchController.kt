package client.cli.controller

import client.cli.HomeAction
import client.cli.state.AnyState
import client.cli.state.MenuState
import client.cli.state.SearchResultState
import domain.SearchMovies

class SearchController(private val searchMovies: SearchMovies) : Controller() {

    override val actions = setOf(
        HomeAction
    )

    override suspend infix fun execute(command: String): AnyState {
        return when (actionBy(command)) {
            HomeAction -> MenuState
            else -> SearchResultState(searchMovies(command))
        }
    }
}
