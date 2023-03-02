package cinescout.details.presentation.model

import cinescout.screenplay.domain.model.Rating

sealed interface MovieDetailsAction {

    object AddToWatchlist : MovieDetailsAction
    data class RateMovie(val rating: Rating) : MovieDetailsAction
    object RemoveFromWatchlist : MovieDetailsAction
}
