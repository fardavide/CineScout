package cinescout.details.presentation.model

import cinescout.screenplay.domain.model.Rating

sealed interface TvShowDetailsAction {

    object AddToWatchlist : TvShowDetailsAction
    data class RateTvShow(val rating: Rating) : TvShowDetailsAction
    object RemoveFromWatchlist : TvShowDetailsAction
}
