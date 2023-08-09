package cinescout.report.presentation.viewmodel

import androidx.compose.runtime.Composable
import cinescout.report.presentation.action.FeatureRequestAction
import cinescout.report.presentation.presenter.FeatureRequestPresenter
import cinescout.report.presentation.state.FeatureRequestState
import cinescout.utils.compose.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class FeatureRequestViewModel(
    private val presenter: FeatureRequestPresenter
) : MoleculeViewModel<FeatureRequestAction, FeatureRequestState>() {

    @Composable
    override fun models(actions: Flow<FeatureRequestAction>): FeatureRequestState = presenter.models(actions)
}
