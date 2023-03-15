package cinescout.lists.presentation.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.molecule.RecompositionClock.Immediate
import app.cash.paging.PagingData
import app.cash.paging.map
import cinescout.lists.presentation.action.ItemsListAction
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
    private val listItemUiModelMapper: ListItemUiModelMapper
) : MoleculeViewModel<ItemsListAction, ItemsListState>() {

    private val mutableFilter: MutableStateFlow<ListFilter> = MutableStateFlow(ListFilter.Watchlist)
    private val mutableType: MutableStateFlow<ScreenplayType> = MutableStateFlow(ScreenplayType.All)

    override val state = launchMolecule(clock = Immediate) {
        val filter = mutableFilter.collectAsState().value
        val type = mutableType.collectAsState().value

        val items = itemsFlow(filter, type).collectAsLazyPagingItems()

        ItemsListState(
            filter = filter,
            items = items,
            type = type
        )
    }

    override fun submit(action: ItemsListAction) {
        when (action) {
            is ItemsListAction.SelectFilter -> onSelectFilter(action.filter)
            is ItemsListAction.SelectType -> onSelectType(action.listType)
        }
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
