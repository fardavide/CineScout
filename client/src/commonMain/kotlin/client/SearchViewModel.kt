package client

import domain.SearchMovies
import entities.movies.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class SearchViewModel(
    override val scope: CoroutineScope,
    dispatchers: DispatchersProvider,
    private val searchMovies: SearchMovies,
) : CineViewModel, DispatchersProvider by dispatchers {

    val result = ViewStateFlow<Collection<Movie>>()

    fun search(query: String) {
        scope.launch {

            result.emitCatching {
                searchMovies(query)
            }
        }
    }
}
