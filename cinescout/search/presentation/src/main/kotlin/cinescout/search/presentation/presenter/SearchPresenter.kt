package cinescout.search.presentation.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paging.map
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.search.domain.usecase.SearchPagedScreenplays
import cinescout.search.presentation.action.SearchAction
import cinescout.search.presentation.model.SearchItemUiModel
import cinescout.search.presentation.state.SearchState
import cinescout.utils.compose.debouncePagingItems
import cinescout.utils.compose.paging.PagingItemsStateMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
internal class SearchPresenter(
    private val pagingItemsStateMapper: PagingItemsStateMapper,
    private val searchScreenplays: SearchPagedScreenplays
) {

    @Composable
    fun models(actions: Flow<SearchAction>): SearchState {
        var query by remember { mutableStateOf("") }

        val items = remember(query) {
            searchScreenplays(ScreenplayType.All, query).map { pagingData ->
                pagingData.map { screenplay ->
                    SearchItemUiModel(
                        screenplayIds = screenplay.ids,
                        title = screenplay.title
                    )
                }
            }
        }.collectAsLazyPagingItems()

        LaunchedEffect(Unit) {
            actions.collect { action ->
                when (action) {
                    is SearchAction.Search -> query = action.query
                }
            }
        }

        return debouncePagingItems(itemsState = pagingItemsStateMapper.toState(items)) { itemsState ->
            SearchState(
                query = query,
                itemsState = itemsState
            )
        }
    }
}
