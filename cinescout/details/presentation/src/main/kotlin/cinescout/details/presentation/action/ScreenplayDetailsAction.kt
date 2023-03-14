package cinescout.details.presentation.action

import cinescout.screenplay.domain.model.Rating

sealed interface ScreenplayDetailsAction {

    object AddToWatchlist : ScreenplayDetailsAction
    data class Rate(val rating: Rating) : ScreenplayDetailsAction
    object RemoveFromWatchlist : ScreenplayDetailsAction
}
