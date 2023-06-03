package cinescout.details.domain.model

import arrow.core.Option
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.media.domain.model.ScreenplayMedia
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.rating.domain.model.PersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.watchlist.domain.model.IsInWatchlist

sealed interface WithScreenplay {
    val screenplay: Screenplay
}

@Suppress("DataClassShouldBeImmutable")
data class ScreenplayWithExtra(
    override val screenplay: Screenplay
) : WithScreenplay, WithCredits, WithGenres, WithHistory, WithKeywords, WithMedia, WithPersonalRating, WithWatchlist {

    override lateinit var credits: ScreenplayCredits
    override lateinit var genres: ScreenplayGenres
    override lateinit var history: ScreenplayHistory
    override lateinit var isInWatchlistBoxed: IsInWatchlist
    override lateinit var keywords: ScreenplayKeywords
    override lateinit var media: ScreenplayMedia
    override lateinit var personalRatingBoxed: PersonalRating
}

fun ScreenplayWithExtra(
    screenplay: Screenplay,
    credits: ScreenplayCredits,
    genres: ScreenplayGenres,
    history: ScreenplayHistory,
    isInWatchlist: Boolean,
    keywords: ScreenplayKeywords,
    media: ScreenplayMedia,
    personalRating: Option<Rating>
): ScreenplayWithExtra = ScreenplayWithExtra(screenplay).apply {
    this.credits = credits
    this.genres = genres
    this.history = history
    this.isInWatchlistBoxed = IsInWatchlist(isInWatchlist)
    this.keywords = keywords
    this.media = media
    this.personalRatingBoxed = PersonalRating(personalRating)
}
