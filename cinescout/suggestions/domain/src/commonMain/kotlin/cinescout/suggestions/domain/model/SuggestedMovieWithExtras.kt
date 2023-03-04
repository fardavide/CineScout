package cinescout.suggestions.domain.model

import arrow.core.Option
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieKeywords
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.Rating

data class SuggestedMovieWithExtras(
    val movie: Movie,
    val affinity: Affinity,
    val genres: List<Genre>,
    val credits: MovieCredits,
    val isInWatchlist: Boolean,
    val keywords: MovieKeywords,
    val personalRating: Option<Rating>,
    val source: SuggestionSource
)
