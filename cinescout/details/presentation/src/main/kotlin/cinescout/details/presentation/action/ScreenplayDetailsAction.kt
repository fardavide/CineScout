package cinescout.details.presentation.action

import cinescout.history.domain.usecase.AddToHistory
import cinescout.screenplay.domain.model.Rating

sealed interface ScreenplayDetailsAction {

    data class AddItemToHistory(val params: AddToHistory.Params) : ScreenplayDetailsAction

    data class Rate(val rating: Rating) : ScreenplayDetailsAction

    object ToggleWatchlist : ScreenplayDetailsAction
}
