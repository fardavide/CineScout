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
import cinescout.search.presentation.model.SearchLikeItemAction
import cinescout.search.presentation.model.SearchLikedItemState
import cinescout.search.presentation.model.SearchLikedItemType
import cinescout.search.presentation.model.SearchLikedItemUiModel
import cinescout.search.presentation.model.toScreenplayType
import cinescout.voting.domain.usecase.SetLiked
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
internal class SearchLikedItemPresenter(
    private val searchScreenplays: SearchPagedScreenplays,
    private val setLiked: SetLiked
) {

    @Composable
    fun models(actionsFlow: Flow<SearchLikeItemAction>): SearchLikedItemState {
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

        LaunchedEffect(Unit) {
            actionsFlow.collect { action ->
                when (action) {
                    is SearchLikeItemAction.LikeItem -> setLiked(action.itemId)
                    is SearchLikeItemAction.Search -> { query = action.query }
                    is SearchLikeItemAction.SelectItemType -> { type = action.itemType }
                }
            }
        }

        return SearchLikedItemState(query = query, items = items)
    }
}
