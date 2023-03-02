package cinescout.tvshows.domain.model

import arrow.core.Option
import cinescout.screenplay.domain.model.Rating

data class TvShowWithExtras(
    val tvShowWithDetails: TvShowWithDetails,
    val credits: TvShowCredits,
    val isInWatchlist: Boolean,
    val keywords: TvShowKeywords,
    val personalRating: Option<Rating>
)
