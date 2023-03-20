package cinescout.search.presentation.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paging.map
import cinescout.search.domain.usecase.SearchPagedScreenplays
import cinescout.search.presentation.action.SearchLikeItemAction
import cinescout.search.presentation.model.SearchLikedItemType
import cinescout.search.presentation.model.SearchLikedItemUiModel
import cinescout.search.presentation.model.toScreenplayType
import cinescout.search.presentation.state.SearchLikedItemState
import cinescout.utils.compose.paging.PagingItemsState
import cinescout.utils.compose.paging.PagingItemsStateMapper
import cinescout.voting.domain.usecase.SetLiked
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import kotlin.time.Duration.Companion.milliseconds

@Factory
internal class SearchLikedItemPresenter(
    private val pagingItemsStateMapper: PagingItemsStateMapper,
    private val searchScreenplays: SearchPagedScreenplays,
    private val setLiked: SetLiked
) {

    @Composable
    fun models(actions: Flow<SearchLikeItemAction>): SearchLikedItemState {
        var query by remember { mutableStateOf("") }
        var type by remember { mutableStateOf(SearchLikedItemType.Movies) }

        val items = remember(query, type) {
            searchScreenplays(type.toScreenplayType(), query).map { pagingData ->
                pagingData.map { screenplay ->
                    SearchLikedItemUiModel(
                        screenplayId = screenplay.tmdbId,
                        title = screenplay.title
                    )
                }
            }
        }.collectAsLazyPagingItems()

        val itemsState = pagingItemsStateMapper.toState(items)

        LaunchedEffect(Unit) {
            actions.collect { action ->
                when (action) {
                    is SearchLikeItemAction.LikeItem -> setLiked(action.itemId)
                    is SearchLikeItemAction.Search -> { query = action.query }
                    is SearchLikeItemAction.SelectItemType -> { type = action.itemType }
                }
            }
        }

        return withCache(itemsState = itemsState) {
            SearchLikedItemState(
                query = query,
                itemsState = it
            )
        }
    }

    @Composable
    private fun withCache(
        itemsState: PagingItemsState<SearchLikedItemUiModel>,
        block: (PagingItemsState<SearchLikedItemUiModel>) -> SearchLikedItemState
    ): SearchLikedItemState {
        var prevItemsState: PagingItemsState<SearchLikedItemUiModel> by remember {
            mutableStateOf(PagingItemsState.Empty)
        }

        val cacheEnabledState = when (itemsState) {
            is PagingItemsState.Error, is PagingItemsState.NotEmpty -> {
                prevItemsState = itemsState
                itemsState
            }
            PagingItemsState.Empty -> {
                LaunchedEffect(itemsState) {
                    delay(EmptyDelay)
                    prevItemsState = itemsState
                }
                prevItemsState
            }
            PagingItemsState.Loading -> {
                prevItemsState = when (val prev = prevItemsState) {
                    PagingItemsState.Empty, is PagingItemsState.Error, PagingItemsState.Loading -> itemsState
                    is PagingItemsState.NotEmpty -> prev.copy(isAlsoLoading = true)
                }
                prevItemsState
            }
        }
        return block(cacheEnabledState)
    }

    companion object {

        val EmptyDelay = 100.milliseconds
    }
}
