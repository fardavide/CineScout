package cinescout.details.presentation.model

import cinescout.common.model.Rating

sealed interface TvShowDetailsAction {

    object AddToWatchlist : TvShowDetailsAction
    data class RateTvShow(val rating: Rating) : TvShowDetailsAction
    object RemoveFromWatchlist : TvShowDetailsAction
}
