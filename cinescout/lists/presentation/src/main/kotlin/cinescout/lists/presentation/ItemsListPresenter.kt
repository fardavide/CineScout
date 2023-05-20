package cinescout.lists.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paging.map
import arrow.core.getOrElse
import cinescout.lists.domain.ListSorting
import cinescout.lists.presentation.action.ItemsListAction
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.mapper.SavedListOptionsMapper
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.lists.presentation.model.ListOptionUiModel
import cinescout.lists.presentation.state.ItemsListState
import cinescout.rating.domain.usecase.GetPagedPersonalRatings
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.settings.domain.usecase.GetSavedListOptions
import cinescout.settings.domain.usecase.UpdateSavedListOptions
import cinescout.utils.compose.Effect
import cinescout.utils.compose.paging.PagingItemsStateMapper
import cinescout.voting.domain.usecase.GetPagedDislikedScreenplays
import cinescout.voting.domain.usecase.GetPagedLikedScreenplays
import cinescout.watchlist.domain.usecase.GetPagedWatchlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
internal class ItemsListPresenter(
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
        var filter: ListFilter by remember { mutableStateOf(listOption.listFilter) }
        var sorting: ListSorting by remember { mutableStateOf(listOption.listSorting) }
        var type: ScreenplayTypeFilter by remember { mutableStateOf(listOption.screenplayTypeFilter) }

        val items = remember(filter, sorting, type) {
            itemsFlow(filter, sorting, type)
        }.collectAsLazyPagingItems()
        val itemsState = pagingItemsStateMapper.toState(items)

        val scrollToTop = remember(filter, sorting, type, itemsState.isLoading) {
            when {
                itemsState.isLoading -> Effect.empty()
                else -> Effect.of(Unit)
            }
        }

        LaunchedEffect(Unit) {
            actions.collect { action ->
                when (action) {
                    is ItemsListAction.SelectFilter -> filter = action.filter
                    is ItemsListAction.SelectSorting -> sorting = action.sorting
                    is ItemsListAction.SelectType -> type = action.listType
                }
                saveListOptions(filter, sorting, type)
            }
        }

        return ItemsListState(
            filter = filter,
            itemsState = itemsState,
            scrollToTop = scrollToTop,
            sorting = sorting,
            type = type
        )
    }

    private fun itemsFlow(
        filter: ListFilter,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Flow<PagingData<ListItemUiModel>> = when (filter) {
        ListFilter.Disliked -> dislikedFlow(sorting, type)
        ListFilter.Liked -> likedFlow(sorting, type)
        ListFilter.Rated -> ratedFlow(sorting, type)
        ListFilter.Watchlist -> watchlistFlow(sorting, type)
    }

    private fun dislikedFlow(
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Flow<PagingData<ListItemUiModel>> =
        getPagedDislikedScreenplays(sorting, type).map { it.map(listItemUiModelMapper::toUiModel) }

    private fun likedFlow(
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Flow<PagingData<ListItemUiModel>> =
        getPagedLikedScreenplays(sorting, type).map { it.map(listItemUiModelMapper::toUiModel) }

    private fun ratedFlow(
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Flow<PagingData<ListItemUiModel>> =
        getPagedPersonalRatings(sorting, type).map { it.map(listItemUiModelMapper::toUiModel) }

    private fun watchlistFlow(
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Flow<PagingData<ListItemUiModel>> =
        getPagedWatchlist(sorting, type).map { it.map(listItemUiModelMapper::toUiModel) }

    private suspend fun saveListOptions(
        filter: ListFilter,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ) {
        val listOptions = ListOptionUiModel(
            listFilter = filter,
            listSorting = sorting,
            screenplayTypeFilter = type
        )
        updateSavedListOptions(savedListOptionsMapper.toDomainModel(listOptions))
    }

    companion object {

        val DefaultListOptions = ListOptionUiModel(
            listFilter = ListFilter.Watchlist,
            listSorting = ListSorting.Rating.Descending,
            screenplayTypeFilter = ScreenplayTypeFilter.All
        )
    }
}
