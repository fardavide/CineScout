package cinescout.search.presentation.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paging.map
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.search.domain.usecase.SearchPagedScreenplays
import cinescout.search.presentation.model.SearchLikeItemAction
import cinescout.search.presentation.model.SearchLikedItemState
import cinescout.search.presentation.model.SearchLikedItemType
import cinescout.search.presentation.model.SearchLikedItemUiModel
import cinescout.search.presentation.model.toScreenplayType
import cinescout.utils.compose.MoleculeViewModel
import cinescout.utils.kotlin.combineToPair
import cinescout.voting.domain.usecase.SetLiked
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.time.Duration.Companion.milliseconds

@KoinViewModel
internal class SearchLikedItemViewModel(
    private val searchScreenplays: SearchPagedScreenplays,
    private val setLiked: SetLiked
) : MoleculeViewModel<SearchLikeItemAction, SearchLikedItemState>() {

    private val mutableQuery = MutableStateFlow("")
    private val mutablePagingData: MutableStateFlow<PagingData<SearchLikedItemUiModel>> =
        MutableStateFlow(PagingData.empty())
    private val mutableType = MutableStateFlow(SearchLikedItemType.Movies)

    init {
        viewModelScope.launch {
            combineToPair(mutableQuery, mutableType)
                .debounce(300.milliseconds)
                .onEach { (_, _) ->
                    // TODO: on query update
                }
                .filterNot { (query, _) -> query.isBlank() }
                .flatMapLatest { (query, type) -> searchScreenplays(type.toScreenplayType(), query) }
                .collect { pagingData ->
                    val uiModelPagingData = pagingData.map { screenplay ->
                        SearchLikedItemUiModel(
                            screenplayId = screenplay.tmdbId,
                            title = screenplay.title
                        )
                    }
                    mutablePagingData.emit(uiModelPagingData)
                }
        }
    }

    @Composable
    override fun models(actions: Flow<SearchLikeItemAction>): SearchLikedItemState {
        val query by mutableQuery.collectAsState()
        val items = mutablePagingData.collectAsLazyPagingItems()

        return SearchLikedItemState(
            query = query,
            items = items
        )
    }

    override fun submit(action: SearchLikeItemAction) {
        when (action) {
            is SearchLikeItemAction.LikeItem -> likeItem(action.itemId)
            is SearchLikeItemAction.Search -> updateSearchQuery(action.query)
            is SearchLikeItemAction.SelectItemType -> mutableType.value = action.itemType
        }
    }

    private fun likeItem(itemId: TmdbScreenplayId) {
        viewModelScope.launch { setLiked(itemId) }
    }

    private fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            mutableQuery.emit(query)
        }
    }
}
