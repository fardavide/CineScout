package cinescout.suggestions.domain.model

import arrow.core.Option
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.Rating
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.model.TvShowKeywords

data class SuggestedTvShowWithExtras(
    val tvShow: TvShow,
    val affinity: Affinity,
    val genres: List<Genre>,
    val credits: TvShowCredits,
    val isInWatchlist: Boolean,
    val keywords: TvShowKeywords,
    val personalRating: Option<Rating>,
    val source: SuggestionSource
)
