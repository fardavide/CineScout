package cinescout.search.presentation.viewmodel

import androidx.compose.runtime.Composable
import cinescout.search.presentation.model.SearchLikeItemAction
import cinescout.search.presentation.model.SearchLikedItemState
import cinescout.search.presentation.presenter.SearchLikedItemPresenter
import cinescout.utils.compose.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class SearchLikedItemViewModel(
    private val presenter: SearchLikedItemPresenter
) : MoleculeViewModel<SearchLikeItemAction, SearchLikedItemState>() {

    @Composable
    override fun models(actions: Flow<SearchLikeItemAction>): SearchLikedItemState =
        presenter.models(actionsFlow = actions)
}
