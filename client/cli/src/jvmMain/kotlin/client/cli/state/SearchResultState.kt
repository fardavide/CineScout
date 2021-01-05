package client.cli.state

import client.cli.HomeAction
import client.cli.view.SearchResult
import entities.movies.Movie
import org.koin.core.component.get

class SearchResultState(private val movies: Collection<Movie>) : State() {

    override val actions = setOf(
        HomeAction
    )

    override suspend infix fun execute(command: String): State {
        return when (actionBy(command)) {
            HomeAction -> MenuState
            else -> SearchState(searchViewModel = get()) execute command
        }
    }

    override fun render() = SearchResult(movies, actions).render()
}
