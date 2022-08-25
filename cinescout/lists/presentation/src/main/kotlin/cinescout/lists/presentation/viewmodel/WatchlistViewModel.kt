package cinescout.lists.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.error.DataError
import cinescout.lists.presentation.model.WatchlistAction
import cinescout.lists.presentation.model.WatchlistItemUiModel
import cinescout.lists.presentation.model.WatchlistState
import cinescout.movies.domain.usecase.GetAllWatchlistMovies
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.kotlin.nonEmptyUnsafe
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import store.Refresh

internal class WatchlistViewModel(
    private val errorToMessageMapper: NetworkErrorToMessageMapper,
    private val getAllWatchlistMovies: GetAllWatchlistMovies
) : CineScoutViewModel<WatchlistAction, WatchlistState>(WatchlistState.Loading) {

    init {
        viewModelScope.launch {
            getAllWatchlistMovies(refresh = Refresh.WithInterval()).loadAll().map { moviesEither ->
                moviesEither.fold(
                    ifLeft = { error -> error.toErrorState() },
                    ifRight = { movies ->
                        val items = movies.data.map { movie ->
                            WatchlistItemUiModel(tmdbId = movie.tmdbId, title = movie.title)
                        }
                        if (items.isEmpty()) WatchlistState.Data.Empty
                        else WatchlistState.Data.NotEmpty(items.nonEmptyUnsafe())
                    }
                )
            }.collect { newState ->
                updateState { newState }
            }
        }
    }

    private fun DataError.toErrorState(): WatchlistState.Error = when (this) {
        DataError.Local.NoCache -> TODO()
        is DataError.Remote -> WatchlistState.Error(errorToMessageMapper.toMessage(networkError))
    }

    override fun submit(action: WatchlistAction) {
        // No actions
    }
}
