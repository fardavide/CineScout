package client.viewModel

import client.ViewState
import client.ViewState.Loading
import client.ViewStateFlow
import domain.stats.GetMoviesInWatchlist
import entities.Either
import entities.Error
import entities.movies.Movie
import entities.right
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WatchlistViewModel(
    override val scope: CoroutineScope,
    getMoviesInWatchlist: GetMoviesInWatchlist
) : CineViewModel {

    val result: StateFlow<Either<GetMoviesInWatchlist.Error, GetMoviesInWatchlist.State>> =
        getMoviesInWatchlist().stateIn(
            scope,
            SharingStarted.Eagerly,
            GetMoviesInWatchlist.State.Loading.right()
        )
}
