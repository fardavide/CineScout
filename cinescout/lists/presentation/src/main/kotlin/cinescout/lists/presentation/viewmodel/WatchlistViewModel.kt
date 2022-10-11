package cinescout.lists.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.error.DataError
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.ItemsListState
import cinescout.lists.presentation.model.WatchlistAction
import cinescout.movies.domain.usecase.GetAllWatchlistMovies
import cinescout.tvshows.domain.usecase.GetAllWatchlistTvShows
import cinescout.unsupported
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.kotlin.nonEmptyUnsafe
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import store.Refresh

internal class WatchlistViewModel(
    private val errorToMessageMapper: NetworkErrorToMessageMapper,
    private val getAllWatchlistMovies: GetAllWatchlistMovies,
    private val getAllWatchlistTvShows: GetAllWatchlistTvShows,
    private val listItemUiModelMapper: ListItemUiModelMapper
) : CineScoutViewModel<WatchlistAction, ItemsListState>(ItemsListState.Loading) {

    init {
        viewModelScope.launch {
            getAllWatchlistMovies(refresh = Refresh.WithInterval()).loadAll().map { moviesEither ->
                moviesEither.fold(
                    ifLeft = { error -> error.toErrorState() },
                    ifRight = { movies ->
                        val items = movies.data.map(listItemUiModelMapper::toUiModel)
                        if (items.isEmpty()) ItemsListState.ItemsState.Data.Empty
                        else ItemsListState.ItemsState.Data.NotEmpty(items.nonEmptyUnsafe())
                    }
                )
            }.collect { newItemsState ->
                updateState { currentState -> currentState.copy(items = newItemsState) }
            }
        }
    }

    private fun DataError.toErrorState(): ItemsListState.ItemsState.Error = when (this) {
        DataError.Local.NoCache -> unsupported
        is DataError.Remote -> ItemsListState.ItemsState.Error(errorToMessageMapper.toMessage(networkError))
    }

    override fun submit(action: WatchlistAction) {
        // No actions
    }
}
