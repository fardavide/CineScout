package cinescout.lists.presentation.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paging.PagingData
import app.cash.paging.map
import cinescout.lists.presentation.action.ItemsListAction
import cinescout.lists.presentation.mapper.ItemsListStateMapper
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.lists.presentation.state.ItemsListState
import cinescout.rating.domain.usecase.GetPagedPersonalRatings
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.utils.compose.MoleculeViewModel
import cinescout.voting.domain.usecase.GetPagedDislikedScreenplays
import cinescout.voting.domain.usecase.GetPagedLikedScreenplays
import cinescout.watchlist.domain.usecase.GetPagedWatchlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ItemsListViewModel(
    private val getPagedDislikedScreenplays: GetPagedDislikedScreenplays,
    private val getPagedLikedScreenplays: GetPagedLikedScreenplays,
    private val getPagedPersonalRatings: GetPagedPersonalRatings,
    private val getPagedWatchlist: GetPagedWatchlist,
    private val listItemUiModelMapper: ListItemUiModelMapper,
    private val stateMapper: ItemsListStateMapper
) : MoleculeViewModel<ItemsListAction, ItemsListState>() {

    private val mutableFilter: MutableStateFlow<ListFilter> = MutableStateFlow(ListFilter.Watchlist)
    private val mutableType: MutableStateFlow<ScreenplayType> = MutableStateFlow(ScreenplayType.All)

    @Composable
    override fun models(actions: Flow<ItemsListAction>): ItemsListState {
        val filter by mutableFilter.collectAsState()
        val type by mutableType.collectAsState()

        val items = remember(filter, type) { itemsFlow(filter, type) }.collectAsLazyPagingItems()

        LaunchedEffect(Unit) {
            actions.collect { action ->
                when (action) {
                    is ItemsListAction.SelectFilter -> onSelectFilter(action.filter)
                    is ItemsListAction.SelectType -> onSelectType(action.listType)
                }
            }
        }

        return stateMapper.toState(filter, items, type)
    }

    private fun onSelectFilter(filter: ListFilter) {
        launchInScope { mutableFilter.emit(filter) }
    }

    private fun onSelectType(type: ScreenplayType) {
        launchInScope { mutableType.emit(type) }
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
