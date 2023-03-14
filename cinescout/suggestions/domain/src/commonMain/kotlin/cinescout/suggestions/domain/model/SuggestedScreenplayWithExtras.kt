package cinescout.suggestions.domain.model

import arrow.core.Option
import cinescout.people.domain.model.MovieCredits
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.people.domain.model.TvShowCredits
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.MovieGenres
import cinescout.screenplay.domain.model.MovieKeywords
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.TvShowGenres
import cinescout.screenplay.domain.model.TvShowKeywords

sealed interface SuggestedScreenplayWithExtras {
    val affinity: Affinity
    val credits: ScreenplayCredits
    val genres: ScreenplayGenres
    val isInWatchlist: Boolean
    val keywords: ScreenplayKeywords
    val personalRating: Option<Rating>
    val screenplay: Screenplay
    val source: SuggestionSource
}

@Suppress("LongParameterList")
fun SuggestedScreenplayWithExtras(
    affinity: Affinity,
    credits: ScreenplayCredits,
    genres: ScreenplayGenres,
    isInWatchlist: Boolean,
    keywords: ScreenplayKeywords,
    personalRating: Option<Rating>,
    screenplay: Screenplay,
    source: SuggestionSource
): SuggestedScreenplayWithExtras = when (screenplay) {
    is Movie -> SuggestedMovieWithExtras(
        affinity = affinity,
        credits = credits as MovieCredits,
        genres = genres as MovieGenres,
        isInWatchlist = isInWatchlist,
        keywords = keywords as MovieKeywords,
        personalRating = personalRating,
        screenplay = screenplay,
        source = source
    )
    is TvShow -> SuggestedTvShowWithExtras(
        affinity = affinity,
        credits = credits as TvShowCredits,
        genres = genres as TvShowGenres,
        isInWatchlist = isInWatchlist,
        keywords = keywords as TvShowKeywords,
        personalRating = personalRating,
        screenplay = screenplay,
        source = source
    )
}

data class SuggestedMovieWithExtras(
    override val affinity: Affinity,
    override val credits: MovieCredits,
    override val genres: MovieGenres,
    override val isInWatchlist: Boolean,
    override val keywords: MovieKeywords,
    override val personalRating: Option<Rating>,
    override val screenplay: Movie,
    override val source: SuggestionSource
) : SuggestedScreenplayWithExtras

data class SuggestedTvShowWithExtras(
    override val affinity: Affinity,
    override val credits: TvShowCredits,
    override val genres: TvShowGenres,
    override val isInWatchlist: Boolean,
    override val keywords: TvShowKeywords,
    override val personalRating: Option<Rating>,
    override val screenplay: TvShow,
    override val source: SuggestionSource
) : SuggestedScreenplayWithExtras
