package client.viewModel

import client.ViewState
import client.ViewState.Loading
import client.ViewStateFlow
import domain.stats.GetMoviesInWatchlist
import entities.movies.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class WatchlistViewModel(
    override val scope: CoroutineScope,
    private val getMoviesInWatchlist: GetMoviesInWatchlist
) : CineViewModel {

    val result = ViewStateFlow<Collection<Movie>, Error>()

    init {
        result set Loading

        scope.launch {
            try {
                val movies = getMoviesInWatchlist()
                if (movies.isNotEmpty())
                    result set movies
                else
                    result set Error.NoMovies

            } catch (t: Throwable) {
                result set Error.Unknown(t)
            }
        }
    }

    sealed class Error : ViewState.Error() {
        object NoMovies : WatchlistViewModel.Error()
        data class Unknown(override val throwable: Throwable) : WatchlistViewModel.Error()
    }
}
