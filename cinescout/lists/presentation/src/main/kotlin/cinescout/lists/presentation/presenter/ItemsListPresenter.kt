package cinescout.lists.presentation.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paging.map
import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.none
import cinescout.lists.domain.ListParams
import cinescout.lists.domain.ListSorting
import cinescout.lists.presentation.action.ItemsListAction
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.mapper.SavedListOptionsMapper
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.lists.presentation.model.ListOptionUiModel
import cinescout.lists.presentation.state.ItemsListState
import cinescout.rating.domain.usecase.GetPagedPersonalRatings
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.usecase.GetAllKnownGenres
import cinescout.settings.domain.usecase.GetSavedListOptions
import cinescout.settings.domain.usecase.UpdateSavedListOptions
import cinescout.sync.domain.usecase.FetchScreenplaysAsync
import cinescout.utils.compose.Effect
import cinescout.utils.compose.paging.PagingItemsStateMapper
import cinescout.voting.domain.usecase.GetPagedDislikedScreenplays
import cinescout.voting.domain.usecase.GetPagedLikedScreenplays
import cinescout.watchlist.domain.usecase.GetPagedWatchlist
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
internal class ItemsListPresenter(
    private val fetchScreenplaysAsync: FetchScreenplaysAsync,
    private val getAllKnownGenres: GetAllKnownGenres,
    private val getPagedDislikedScreenplays: GetPagedDislikedScreenplays,
    private val getPagedLikedScreenplays: GetPagedLikedScreenplays,
    private val getPagedPersonalRatings: GetPagedPersonalRatings,
    private val getPagedWatchlist: GetPagedWatchlist,
    private val getSavedListOptions: GetSavedListOptions,
    private val listItemUiModelMapper: ListItemUiModelMapper,
    private val pagingItemsStateMapper: PagingItemsStateMapper,
    private val savedListOptionsMapper: SavedListOptionsMapper,
    private val updateSavedListOptions: UpdateSavedListOptions
) {

    @Composable
    fun models(actions: Flow<ItemsListAction>): ItemsListState {
        val listOption = remember {
            getSavedListOptions().value
                .let(savedListOptionsMapper::toUiModel)
                .getOrElse { DefaultListOptions }
        }
        var genreFilter: Option<Genre> by remember { mutableStateOf(listOption.genreFilter) }
        var listFilter: ListFilter by remember { mutableStateOf(listOption.listFilter) }
        var sorting: ListSorting by remember { mutableStateOf(listOption.listSorting) }
        var type: ScreenplayTypeFilter by remember { mutableStateOf(listOption.screenplayTypeFilter) }

        val availableGenres by remember { getAllKnownGenres().map { it.toPersistentList() } }
            .collectAsState(initial = persistentListOf())

        val items = remember(genreFilter, listFilter, sorting, type) {
            itemsFlow(listFilter, ListParams(genreFilter.map(Genre::slug), sorting, type))
        }.collectAsLazyPagingItems()
        val itemsState = pagingItemsStateMapper.toState(items)

        val scrollToTop = remember(genreFilter, listFilter, sorting, type, itemsState.isLoading) {
            when {
                itemsState.isLoading -> Effect.empty()
                else -> Effect.of(Unit)
            }
        }

        LaunchedEffect(Unit) {
            fetchScreenplaysAsync()
            actions.collect { action ->
                when (action) {
                    is ItemsListAction.SelectGenreFilter -> genreFilter = action.genre
                    is ItemsListAction.SelectListFilter -> listFilter = action.filter
                    is ItemsListAction.SelectSorting -> sorting = action.sorting
                    is ItemsListAction.SelectType -> type = action.listType
                }
                saveListOptions(listFilter, sorting, type)
            }
        }

        return ItemsListState(
            availableGenres = availableGenres,
            genreFilter = genreFilter,
            listFilter = listFilter,
            itemsState = itemsState,
            scrollToTop = scrollToTop,
            sorting = sorting,
            type = type
        )
    }

    private fun itemsFlow(listFilter: ListFilter, params: ListParams): Flow<PagingData<ListItemUiModel>> =
        when (listFilter) {
            ListFilter.Disliked -> dislikedFlow(params)
            ListFilter.Liked -> likedFlow(params)
            ListFilter.Rated -> ratedFlow(params)
            ListFilter.Watchlist -> watchlistFlow(params)
        }

    private fun dislikedFlow(params: ListParams): Flow<PagingData<ListItemUiModel>> =
        getPagedDislikedScreenplays(params).map { it.map(listItemUiModelMapper::toUiModel) }

    private fun likedFlow(params: ListParams): Flow<PagingData<ListItemUiModel>> =
        getPagedLikedScreenplays(params).map { it.map(listItemUiModelMapper::toUiModel) }

    private fun ratedFlow(params: ListParams): Flow<PagingData<ListItemUiModel>> =
        getPagedPersonalRatings(params).map { it.map(listItemUiModelMapper::toUiModel) }

    private fun watchlistFlow(params: ListParams): Flow<PagingData<ListItemUiModel>> =
        getPagedWatchlist(params).map { it.map(listItemUiModelMapper::toUiModel) }

    private suspend fun saveListOptions(
        filter: ListFilter,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ) {
        val listOptions = ListOptionUiModel(
            genreFilter = none(),
            listFilter = filter,
            listSorting = sorting,
            screenplayTypeFilter = type
        )
        updateSavedListOptions(savedListOptionsMapper.toDomainModel(listOptions))
    }

    companion object {

        val DefaultListOptions = ListOptionUiModel(
            genreFilter = none(),
            listFilter = ListFilter.Watchlist,
            listSorting = ListSorting.Rating.Descending,
            screenplayTypeFilter = ScreenplayTypeFilter.All
        )
    }
}
