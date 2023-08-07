package cinescout.report.presentation.viewmodel

import androidx.compose.runtime.Composable
import cinescout.report.presentation.action.ReportBugAction
import cinescout.report.presentation.presenter.ReportBugPresenter
import cinescout.report.presentation.state.ReportBugState
import cinescout.utils.compose.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReportBugViewModel(
    private val presenter: ReportBugPresenter
) : MoleculeViewModel<ReportBugAction, ReportBugState>() {

    @Composable
    override fun models(actions: Flow<ReportBugAction>): ReportBugState = presenter.models(actions)
}
