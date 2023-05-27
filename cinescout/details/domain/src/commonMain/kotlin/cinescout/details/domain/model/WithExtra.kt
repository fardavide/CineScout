package cinescout.details.domain.model

import arrow.core.Option
import cinescout.media.domain.model.ScreenplayMedia
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.rating.domain.model.PersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.watchlist.domain.model.IsInWatchlist

sealed interface WithExtra
sealed interface Extra<S : WithExtra>

interface WithCredits : WithExtra {
    val credits: ScreenplayCredits
    companion object : Extra<WithCredits>
}

interface WithGenres : WithExtra {
    val genres: ScreenplayGenres
    companion object : Extra<WithGenres>
}

interface WithKeywords : WithExtra {
    val keywords: ScreenplayKeywords
    companion object : Extra<WithKeywords>
}

interface WithMedia : WithExtra {
    val media: ScreenplayMedia
    companion object : Extra<WithMedia>
}

interface WithPersonalRating : WithExtra {
    val personalRatingBoxed: PersonalRating
    val personalRating: Option<Rating> get() = personalRatingBoxed.value
    companion object : Extra<WithPersonalRating>
}

interface WithWatchlist : WithExtra {
    val isInWatchlistBoxed: IsInWatchlist
    val isInWatchlist: Boolean get() = isInWatchlistBoxed.value
    companion object : Extra<WithWatchlist>
}
