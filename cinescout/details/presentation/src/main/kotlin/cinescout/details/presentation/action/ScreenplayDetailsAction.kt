package cinescout.details.presentation.action

import cinescout.screenplay.domain.model.Rating

sealed interface ScreenplayDetailsAction {

    object AddToHistory : ScreenplayDetailsAction
    data class Rate(val rating: Rating) : ScreenplayDetailsAction
    object ToggleWatchlist : ScreenplayDetailsAction
}
