package cinescout.lists.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.error.DataError
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.ItemsListState
import cinescout.lists.presentation.model.ListType
import cinescout.lists.presentation.model.RatedListAction
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.tvshows.domain.usecase.GetAllRatedTvShows
import cinescout.unsupported
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.kotlin.nonEmptyUnsafe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import store.Refresh

internal class RatedListViewModel(
    private val errorToMessageMapper: NetworkErrorToMessageMapper,
    private val getAllRatedMovies: GetAllRatedMovies,
    private val getAllRatedTvShows: GetAllRatedTvShows,
    private val listItemUiModelMapper: ListItemUiModelMapper
) : CineScoutViewModel<RatedListAction, ItemsListState>(ItemsListState.Loading) {

    private val listTypeState = MutableStateFlow(ListType.All)

    init {
        viewModelScope.launch {
            combine(
                getAllRatedMovies(refresh = Refresh.WithInterval()).loadAll(),
                getAllRatedTvShows(refresh = Refresh.WithInterval()).loadAll(),
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

    override fun submit(action: RatedListAction) {
        when (action) {
            is RatedListAction.SelectListType -> onSelectListType(action.listType)
        }
    }

    private fun onSelectListType(listType: ListType) {
        viewModelScope.launch {
            listTypeState.emit(listType)
        }
    }
}
