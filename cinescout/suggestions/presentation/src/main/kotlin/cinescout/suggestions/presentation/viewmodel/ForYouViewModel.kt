package cinescout.suggestions.presentation.viewmodel

import androidx.compose.runtime.Composable
import cinescout.suggestions.presentation.action.ForYouAction
import cinescout.suggestions.presentation.presenter.ForYouPresenter
import cinescout.suggestions.presentation.state.ForYouState
import cinescout.utils.compose.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ForYouViewModel(
    private val presenter: ForYouPresenter
) : MoleculeViewModel<ForYouAction, ForYouState>() {

    @Composable
    override fun models(actions: Flow<ForYouAction>): ForYouState = presenter.models(actionsFlow = actions)
}
