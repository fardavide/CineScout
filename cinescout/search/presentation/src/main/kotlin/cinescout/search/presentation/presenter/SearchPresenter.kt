package cinescout.search.presentation.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cinescout.search.presentation.action.SearchAction
import cinescout.search.presentation.state.SearchState
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class SearchPresenter {

    @Composable
    fun models(actions: Flow<SearchAction>): SearchState {
        var query by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            actions.collect { action ->
                when (action) {
                    is SearchAction.Search -> query = action.query
                }
            }
        }

        return SearchState(
            query = query
        )
    }
}
