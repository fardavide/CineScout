package cinescout.lists.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.error.DataError
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.ListType
import cinescout.lists.presentation.model.WatchlistAction
import cinescout.lists.presentation.state.ItemsListState
import cinescout.movies.domain.usecase.GetAllWatchlistMovies
import cinescout.tvshows.domain.usecase.GetAllWatchlistTvShows
import cinescout.unsupported
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.kotlin.nonEmptyUnsafe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import store.Refresh

@KoinViewModel
internal class WatchlistViewModel(
    private val errorToMessageMapper: NetworkErrorToMessageMapper,
    private val getAllWatchlistMovies: GetAllWatchlistMovies,
    private val getAllWatchlistTvShows: GetAllWatchlistTvShows,
    private val listItemUiModelMapper: ListItemUiModelMapper
) : CineScoutViewModel<WatchlistAction, ItemsListState>(ItemsListState.Loading) {

    private val listTypeState = MutableStateFlow(ListType.All)

    init {
        viewModelScope.launch {
            combine(
                getAllWatchlistMovies(refresh = Refresh.WithInterval()).loadAll(),
                getAllWatchlistTvShows(refresh = Refresh.WithInterval()).loadAll(),
                listTypeState
            ) { moviesEither, tvShowsEither, listType ->
                either {
                    val movies = moviesEither.bind()
                    val tvShows = tvShowsEither.bind()
                    val uiModels = when (listType) {
                        ListType.All -> movies.data.map(listItemUiModelMapper::toUiModel) +
                            tvShows.data.map(listItemUiModelMapper::toUiModel)
                        ListType.Movies -> movies.data.map(listItemUiModelMapper::toUiModel)
                        ListType.TvShows -> tvShows.data.map(listItemUiModelMapper::toUiModel)
                    }
                    if (uiModels.isEmpty()) ItemsListState.ItemsState.Data.Empty
                    else ItemsListState.ItemsState.Data.NotEmpty(uiModels.nonEmptyUnsafe())
                }.fold(
                    ifLeft = { error -> error.toErrorState() },
                    ifRight = { itemsState -> itemsState }
                ) to listType
            }.collect { (newItemsState, listType) ->
                updateState { currentState -> currentState.copy(items = newItemsState, type = listType) }
            }
        }
    }

    private fun DataError.toErrorState(): ItemsListState.ItemsState.Error = when (this) {
        DataError.Local.NoCache -> unsupported
        is DataError.Remote -> ItemsListState.ItemsState.Error(errorToMessageMapper.toMessage(networkError))
    }

    override fun submit(action: WatchlistAction) {
        when (action) {
            is WatchlistAction.SelectListType -> onSelectListType(action.listType)
        }
    }

    private fun onSelectListType(listType: ListType) {
        viewModelScope.launch {
            listTypeState.emit(listType)
        }
    }
}
