package cinescout.search.presentation.viewmodel

import androidx.compose.runtime.Composable
import cinescout.search.presentation.action.SearchAction
import cinescout.search.presentation.presenter.SearchPresenter
import cinescout.search.presentation.state.SearchState
import cinescout.utils.compose.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class SearchViewModel(
    private val presenter: SearchPresenter
) : MoleculeViewModel<SearchAction, SearchState>() {

    @Composable
    override fun models(actions: Flow<SearchAction>): SearchState = presenter.models(actions)
}
