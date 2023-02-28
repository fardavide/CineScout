package cinescout.lists.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.error.DataError
import cinescout.lists.presentation.action.ItemsListAction
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListType
import cinescout.lists.presentation.state.ItemsListState
import cinescout.movies.domain.usecase.GetAllDislikedMovies
import cinescout.movies.domain.usecase.GetAllLikedMovies
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.movies.domain.usecase.GetAllWatchlistMovies
import cinescout.tvshows.domain.usecase.GetAllDislikedTvShows
import cinescout.tvshows.domain.usecase.GetAllLikedTvShows
import cinescout.tvshows.domain.usecase.GetAllRatedTvShows
import cinescout.tvshows.domain.usecase.GetAllWatchlistTvShows
import cinescout.unsupported
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.kotlin.nonEmptyUnsafe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import store.Refresh

@KoinViewModel
internal class ItemsListViewModel(
    private val errorToMessageMapper: NetworkErrorToMessageMapper,
    private val getAllDislikedMovies: GetAllDislikedMovies,
    private val getAllDislikedTvShows: GetAllDislikedTvShows,
    private val getAllLikedMovies: GetAllLikedMovies,
    private val getAllLikedTvShows: GetAllLikedTvShows,
    private val getAllRatedMovies: GetAllRatedMovies,
    private val getAllRatedTvShows: GetAllRatedTvShows,
    private val getAllWatchlistMovies: GetAllWatchlistMovies,
    private val getAllWatchlistTvShows: GetAllWatchlistTvShows,
    private val listItemUiModelMapper: ListItemUiModelMapper
) : CineScoutViewModel<ItemsListAction, ItemsListState>(ItemsListState.Loading) {

    private val filterAndType: Flow<Pair<ListFilter, ListType>> =
        state.map { (_, filter, type) -> filter to type }.distinctUntilChanged()

    init {
        viewModelScope.launch {
            filterAndType.flatMapLatest(::itemsFlow).collect { newItemsState ->
                updateState { currentState -> currentState.copy(items = newItemsState) }
            }
        }
    }

    override fun submit(action: ItemsListAction) {
        when (action) {
            is ItemsListAction.SelectFilter -> onSelectFilter(action.filter)
            is ItemsListAction.SelectType -> onSelectType(action.listType)
        }
    }

    private fun onSelectFilter(filter: ListFilter) {
        updateState { currentState -> currentState.copy(filter = filter) }
    }

    private fun onSelectType(type: ListType) {
        updateState { currentState -> currentState.copy(type = type) }
    }

    private fun itemsFlow(filterAndType: Pair<ListFilter, ListType>): Flow<ItemsListState.ItemsState> =
        itemsFlow(filterAndType.first, filterAndType.second)

    private fun itemsFlow(filter: ListFilter, type: ListType): Flow<ItemsListState.ItemsState> =
        when (filter) {
            ListFilter.Disliked -> dislikedFlow(type)
            ListFilter.Liked -> likedFlow(type)
            ListFilter.Rated -> ratedFlow(type)
            ListFilter.Watchlist -> watchlistFlow(type)
        }

    private fun dislikedFlow(type: ListType): Flow<ItemsListState.ItemsState> = combine(
        getAllDislikedMovies(),
        getAllDislikedTvShows()
    ) { movies, tvShows ->
        val uiModels = when (type) {
            ListType.All -> movies.map(listItemUiModelMapper::toUiModel) +
                tvShows.map(listItemUiModelMapper::toUiModel)

            ListType.Movies -> movies.map(listItemUiModelMapper::toUiModel)
            ListType.TvShows -> tvShows.map(listItemUiModelMapper::toUiModel)
        }
        if (uiModels.isEmpty()) ItemsListState.ItemsState.Data.Empty
        else ItemsListState.ItemsState.Data.NotEmpty(uiModels.nonEmptyUnsafe())
    }

    private fun likedFlow(type: ListType): Flow<ItemsListState.ItemsState> = combine(
        getAllLikedMovies(),
        getAllLikedTvShows()
    ) { movies, tvShows ->
        val uiModels = when (type) {
            ListType.All -> movies.map(listItemUiModelMapper::toUiModel) +
                tvShows.map(listItemUiModelMapper::toUiModel)

            ListType.Movies -> movies.map(listItemUiModelMapper::toUiModel)
            ListType.TvShows -> tvShows.map(listItemUiModelMapper::toUiModel)
        }
        if (uiModels.isEmpty()) ItemsListState.ItemsState.Data.Empty
        else ItemsListState.ItemsState.Data.NotEmpty(uiModels.nonEmptyUnsafe())
    }

    private fun ratedFlow(type: ListType): Flow<ItemsListState.ItemsState> = combine(
        getAllRatedMovies(refresh = DefaultRefresh).loadAll(),
        getAllRatedTvShows(refresh = DefaultRefresh).loadAll()
    ) { moviesEither, tvShowsEither ->
        either {
            val movies = moviesEither.bind()
            val tvShows = tvShowsEither.bind()
            val uiModels = when (type) {
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
    }

    private fun watchlistFlow(type: ListType): Flow<ItemsListState.ItemsState> = combine(
        getAllWatchlistMovies(refresh = DefaultRefresh).loadAll(),
        getAllWatchlistTvShows(refresh = DefaultRefresh).loadAll()
    ) { moviesEither, tvShowsEither ->
        either {
            val movies = moviesEither.bind()
            val tvShows = tvShowsEither.bind()
            val uiModels = when (type) {
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
    }

    private fun DataError.toErrorState(): ItemsListState.ItemsState.Error = when (this) {
        DataError.Local.NoCache -> unsupported
        is DataError.Remote -> ItemsListState.ItemsState.Error(errorToMessageMapper.toMessage(networkError))
    }

    companion object {

        private val DefaultRefresh = Refresh.WithInterval()
    }
}
