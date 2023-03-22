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
import cinescout.lists.presentation.action.ItemsListAction
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.lists.presentation.state.ItemsListState
import cinescout.rating.domain.usecase.GetPagedPersonalRatings
import cinescout.screenplay.domain.model.ScreenplayType
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
    private val listItemUiModelMapper: ListItemUiModelMapper,
    private val pagingItemsStateMapper: PagingItemsStateMapper
) {

    @Composable
    fun models(actions: Flow<ItemsListAction>): ItemsListState {
        var filter by remember { mutableStateOf(ListFilter.Watchlist) }
        var type by remember { mutableStateOf(ScreenplayType.All) }

        val items = remember(filter, type) { itemsFlow(filter, type) }.collectAsLazyPagingItems()

        LaunchedEffect(Unit) {
            actions.collect { action ->
                when (action) {
                    is ItemsListAction.SelectFilter -> { filter = action.filter }
                    is ItemsListAction.SelectType -> { type = action.listType }
                }
            }
        }

        return ItemsListState(
            filter = filter,
            itemsState = pagingItemsStateMapper.toState(items),
            type = type
        )
    }

    private fun itemsFlow(filter: ListFilter, type: ScreenplayType): Flow<PagingData<ListItemUiModel>> =
        when (filter) {
            ListFilter.Disliked -> dislikedFlow(type)
            ListFilter.Liked -> likedFlow(type)
            ListFilter.Rated -> ratedFlow(type)
            ListFilter.Watchlist -> watchlistFlow(type)
        }

    private fun dislikedFlow(type: ScreenplayType): Flow<PagingData<ListItemUiModel>> =
        getPagedDislikedScreenplays(type).map { it.map(listItemUiModelMapper::toUiModel) }

    private fun likedFlow(type: ScreenplayType): Flow<PagingData<ListItemUiModel>> =
        getPagedLikedScreenplays(type).map { it.map(listItemUiModelMapper::toUiModel) }

    private fun ratedFlow(type: ScreenplayType): Flow<PagingData<ListItemUiModel>> =
        getPagedPersonalRatings(type).map { it.map(listItemUiModelMapper::toUiModel) }

    private fun watchlistFlow(type: ScreenplayType): Flow<PagingData<ListItemUiModel>> =
        getPagedWatchlist(type).map { it.map(listItemUiModelMapper::toUiModel) }
}