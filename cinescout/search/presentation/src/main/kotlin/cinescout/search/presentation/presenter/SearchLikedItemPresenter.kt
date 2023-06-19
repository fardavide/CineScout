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
import cinescout.utils.compose.debouncePagingItems
import cinescout.utils.compose.paging.PagingItemsStateMapper
import cinescout.voting.domain.usecase.SetLiked
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

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
                        screenplayIds = screenplay.ids,
                        title = screenplay.title
                    )
                }
            }
        }.collectAsLazyPagingItems()

        LaunchedEffect(Unit) {
            actions.collect { action ->
                when (action) {
                    is SearchLikeItemAction.LikeItem -> setLiked(action.itemId)
                    is SearchLikeItemAction.Search -> query = action.query
                    is SearchLikeItemAction.SelectItemType -> type = action.itemType
                }
            }
        }

        return debouncePagingItems(itemsState = pagingItemsStateMapper.toState(items)) { itemsState ->
            SearchLikedItemState(
                query = query,
                itemsState = itemsState
            )
        }
    }
}
