package cinescout.search.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.toNonEmptyListOrNone
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.error.DataError
import cinescout.screenplay.domain.model.Screenplay
import cinescout.search.domain.usecase.SearchPagedScreenplays
import cinescout.search.presentation.model.SearchLikeItemAction
import cinescout.search.presentation.model.SearchLikedItemEvent
import cinescout.search.presentation.model.SearchLikedItemId
import cinescout.search.presentation.model.SearchLikedItemOperation
import cinescout.search.presentation.model.SearchLikedItemState
import cinescout.search.presentation.model.SearchLikedItemType
import cinescout.search.presentation.model.SearchLikedItemUiModel
import cinescout.search.presentation.model.toScreenplayId
import cinescout.search.presentation.model.toScreenplayType
import cinescout.search.presentation.reducer.SearchLikedItemReducer
import cinescout.unsupported
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.android.Reducer
import cinescout.utils.kotlin.combineToPair
import cinescout.voting.domain.usecase.SetLiked
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.time.Duration.Companion.milliseconds

@KoinViewModel
internal class SearchLikedItemViewModel(
    private val networkErrorToMessageMapper: NetworkErrorToMessageMapper,
    reducer: SearchLikedItemReducer,
    private val searchScreenplays: SearchPagedScreenplays,
    private val setLiked: SetLiked
) : CineScoutViewModel<SearchLikeItemAction, SearchLikedItemState>(initialState = SearchLikedItemState.Idle),
    Reducer<SearchLikedItemState, SearchLikedItemOperation> by reducer {

    private val searchQuery = MutableStateFlow("")
    private val type = MutableStateFlow(SearchLikedItemType.Movies)

    init {
        viewModelScope.launch {
            combineToPair(searchQuery, type)
                .debounce(300.milliseconds)
                .onEach { (query, _) ->
                    updateState { currentState ->
                        currentState.reduce(SearchLikedItemEvent.QueryUpdated(query = query))
                    }
                }
                .filterNot { (query, _) -> query.isBlank() }
                .flatMapLatest { (query, type) ->
                    searchScreenplays(type.toScreenplayType(), query).map { moviesEither ->
                        moviesEither.fold(
                            ifLeft = ::toSearchResultError,
                            ifRight = { toSearchResult(it.data) }
                        )
                    }
                }
                .collect { searchResult ->
                    updateState { currentState ->
                        currentState.reduce(SearchLikedItemEvent.SearchResult(result = searchResult))
                    }
                }
        }
    }

    override fun submit(action: SearchLikeItemAction) {
        when (action) {
            is SearchLikeItemAction.LikeItem -> likeItem(action.itemId)
            is SearchLikeItemAction.Search -> updateSearchQuery(action.query)
            is SearchLikeItemAction.SelectItemType -> type.value = action.itemType
        }
    }

    private fun likeItem(itemId: SearchLikedItemId) {
        viewModelScope.launch { setLiked(itemId.toScreenplayId()) }
    }

    private fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            searchQuery.emit(query)
        }
    }

    private fun toSearchResultError(dataError: DataError): SearchLikedItemState.SearchResult.Error =
        when (dataError) {
            DataError.Local.NoCache -> unsupported
            is DataError.Remote -> {
                val message = networkErrorToMessageMapper.toMessage(dataError.networkError)
                SearchLikedItemState.SearchResult.Error(message = message)
            }
        }

    private fun toSearchResult(screenplays: List<Screenplay>): SearchLikedItemState.SearchResult =
        screenplays.toNonEmptyListOrNone().fold(
            ifEmpty = { SearchLikedItemState.SearchResult.NoResults },
            ifSome = { nonEmptyList ->
                val uiModels = nonEmptyList.map { screenplay ->
                    SearchLikedItemUiModel(
                        screenplayId = screenplay.tmdbId,
                        title = screenplay.title
                    )
                }
                SearchLikedItemState.SearchResult.Data(uiModels)
            }
        )
}
