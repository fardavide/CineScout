package cinescout.lists.presentation.viewmodel

import androidx.compose.runtime.collectAsState
import app.cash.molecule.RecompositionClock.ContextClock
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.map
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.error.NetworkError
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
    private val errorToMessageMapper: NetworkErrorToMessageMapper,
    private val getPagedDislikedScreenplays: GetPagedDislikedScreenplays,
    private val getPagedLikedScreenplays: GetPagedLikedScreenplays,
    private val getPagedPersonalRatings: GetPagedPersonalRatings,
    private val getPagedWatchlist: GetPagedWatchlist,
    private val listItemUiModelMapper: ListItemUiModelMapper
) : MoleculeViewModel<ItemsListAction, ItemsListState>() {

    private val filterAndType: MutableStateFlow<Pair<ListFilter, ScreenplayType>> =
        MutableStateFlow(ListFilter.Watchlist to ScreenplayType.All)

    override val state = launchMolecule(clock = ContextClock) {
        val (filter, type) = filterAndType.collectAsState().value

        val items = itemsFlow(filter, type)
            .collectAsLazyPagingItems()

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
        launchInScope {
            filterAndType.emit(filter to filterAndType.value.second)
        }
    }

    private fun onSelectType(type: ScreenplayType) {
        launchInScope {
            filterAndType.emit(filterAndType.value.first to type)
        }
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

    private fun NetworkError.toErrorState(): ItemsListState.ItemsState.Error =
        ItemsListState.ItemsState.Error(errorToMessageMapper.toMessage(this))
}
