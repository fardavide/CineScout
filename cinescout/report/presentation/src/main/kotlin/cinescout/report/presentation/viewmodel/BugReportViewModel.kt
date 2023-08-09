package cinescout.report.presentation.viewmodel

import androidx.compose.runtime.Composable
import cinescout.report.presentation.action.BugReportAction
import cinescout.report.presentation.presenter.BugReportPresenter
import cinescout.report.presentation.state.BugReportState
import cinescout.utils.compose.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class BugReportViewModel(
    private val presenter: BugReportPresenter
) : MoleculeViewModel<BugReportAction, BugReportState>() {

    @Composable
    override fun models(actions: Flow<BugReportAction>): BugReportState = presenter.models(actions)
}
