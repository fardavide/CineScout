package client.viewModel

import client.ViewState
import client.ViewState.Loading
import client.ViewStateFlow
import client.ViewStateFlow.Companion.invoke
import domain.GetMoviesInWatchlist
import entities.movies.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import util.DispatchersProvider

class WatchlistViewModel(
    override val scope: CoroutineScope,
    dispatchers: DispatchersProvider,
    private val getMoviesInWatchlist: GetMoviesInWatchlist
) : CineViewModel, DispatchersProvider by dispatchers {

    val result = ViewStateFlow<Collection<Movie>, Error>()

    init {
        result set Loading

        scope.launch(Io) {
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
