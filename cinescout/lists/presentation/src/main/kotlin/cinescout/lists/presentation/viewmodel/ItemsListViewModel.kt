package cinescout.lists.presentation.viewmodel

import androidx.compose.runtime.Composable
import cinescout.lists.presentation.ItemsListPresenter
import cinescout.lists.presentation.action.ItemsListAction
import cinescout.lists.presentation.state.ItemsListState
import cinescout.utils.compose.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ItemsListViewModel(
    private val presenter: ItemsListPresenter
) : MoleculeViewModel<ItemsListAction, ItemsListState>() {

    @Composable
    override fun models(actions: Flow<ItemsListAction>): ItemsListState = presenter.models(actions = actions)
}
