package cinescout.settings.presentation.viewmodel

import androidx.compose.runtime.Composable
import cinescout.settings.presentation.action.SettingsAction
import cinescout.settings.presentation.presenter.SettingsPresenter
import cinescout.settings.presentation.state.SettingsState
import cinescout.utils.compose.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class SettingsViewModel(
    private val presenter: SettingsPresenter
) : MoleculeViewModel<SettingsAction, SettingsState>() {

    @Composable
    override fun models(actions: Flow<SettingsAction>): SettingsState = presenter.models(actions)
}
