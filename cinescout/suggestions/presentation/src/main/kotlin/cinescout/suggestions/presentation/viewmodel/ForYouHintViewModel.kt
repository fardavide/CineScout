package cinescout.suggestions.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.settings.domain.usecase.SetForYouHintShown
import cinescout.suggestions.presentation.model.ForYouHintAction
import cinescout.utils.android.CineScoutViewModel
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ForYouHintViewModel(
    private val setForYouHintShown: SetForYouHintShown
) : CineScoutViewModel<ForYouHintAction, Unit>(Unit) {

    override fun submit(action: ForYouHintAction) {
        when (action) {
            is ForYouHintAction.Dismiss -> onDismiss()
        }
    }

    private fun onDismiss() {
        viewModelScope.launch {
            setForYouHintShown()
        }
    }
}
