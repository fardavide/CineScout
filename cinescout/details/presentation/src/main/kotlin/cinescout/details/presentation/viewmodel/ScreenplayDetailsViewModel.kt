package cinescout.details.presentation.viewmodel

import androidx.compose.runtime.Composable
import cinescout.details.presentation.action.ScreenplayDetailsAction
import cinescout.details.presentation.presenter.ScreenplayDetailsPresenter
import cinescout.details.presentation.state.ScreenplayDetailsState
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.utils.compose.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
internal class ScreenplayDetailsViewModel(
    @InjectedParam private val screenplayId: TmdbScreenplayId,
    private val presenter: ScreenplayDetailsPresenter
) : MoleculeViewModel<ScreenplayDetailsAction, ScreenplayDetailsState>() {

    @Composable
    override fun models(actions: Flow<ScreenplayDetailsAction>): ScreenplayDetailsState =
        presenter.models(screenplayId, actions = actions)
}
