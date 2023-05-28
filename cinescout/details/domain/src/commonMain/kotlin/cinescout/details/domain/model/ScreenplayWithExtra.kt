package cinescout.details.domain.model

import cinescout.history.domain.model.ScreenplayHistory
import cinescout.media.domain.model.ScreenplayMedia
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.rating.domain.model.PersonalRating
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
