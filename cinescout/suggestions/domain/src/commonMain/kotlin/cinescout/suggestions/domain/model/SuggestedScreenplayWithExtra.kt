package cinescout.suggestions.domain.model

import arrow.core.Option
import cinescout.details.domain.model.WithCredits
import cinescout.details.domain.model.WithGenres
import cinescout.details.domain.model.WithKeywords
import cinescout.details.domain.model.WithMedia
import cinescout.details.domain.model.WithPersonalRating
import cinescout.details.domain.model.WithWatchlist
import cinescout.media.domain.model.ScreenplayMedia
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.rating.domain.model.PersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.watchlist.domain.model.IsInWatchlist

sealed interface WithSuggestedScreenplay {
    val affinity: Affinity
    val screenplay: Screenplay
    val source: SuggestionSource
}

@Suppress("DataClassShouldBeImmutable")
data class SuggestedScreenplayWithExtra(
    override val affinity: Affinity,
    override val screenplay: Screenplay,
    override val source: SuggestionSource
) : WithSuggestedScreenplay, WithCredits, WithGenres, WithKeywords, WithMedia, WithPersonalRating, WithWatchlist {

    override lateinit var credits: ScreenplayCredits
    override lateinit var genres: ScreenplayGenres
    override lateinit var isInWatchlistBoxed: IsInWatchlist
    override lateinit var keywords: ScreenplayKeywords
    override lateinit var media: ScreenplayMedia
    override lateinit var personalRatingBoxed: PersonalRating
}

@Suppress("LongParameterList")
fun SuggestedScreenplayWithExtra(
    affinity: Affinity,
    credits: ScreenplayCredits,
    genres: ScreenplayGenres,
    isInWatchlist: Boolean,
    keywords: ScreenplayKeywords,
    media: ScreenplayMedia,
    personalRating: Option<Rating>,
    screenplay: Screenplay,
    source: SuggestionSource
): SuggestedScreenplayWithExtra = SuggestedScreenplayWithExtra(
    affinity = affinity,
    screenplay = screenplay,
    source = source
).apply {
    this.credits = credits
    this.genres = genres
    this.isInWatchlistBoxed = IsInWatchlist(isInWatchlist)
    this.keywords = keywords
    this.media = media
    this.personalRatingBoxed = PersonalRating(personalRating)
}
