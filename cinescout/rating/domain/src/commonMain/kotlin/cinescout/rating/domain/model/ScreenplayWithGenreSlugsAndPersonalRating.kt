package cinescout.rating.domain.model

import arrow.core.Nel
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.id.GenreSlug
import cinescout.screenplay.domain.model.id.TmdbScreenplayId

sealed interface ScreenplayWithGenreSlugsAndPersonalRating {

    val genreSlugs: Nel<GenreSlug>
    val personalRating: Rating
    val screenplay: Screenplay
}

fun ScreenplayWithGenreSlugsAndPersonalRating(
    genreSlugs: Nel<GenreSlug>,
    personalRating: Rating,
    screenplay: Screenplay
): ScreenplayWithGenreSlugsAndPersonalRating = when (screenplay) {
    is Movie -> MovieWithGenreSlugsAndPersonalRating(
        genreSlugs = genreSlugs,
        personalRating = personalRating,
        screenplay = screenplay
    )
    is TvShow -> TvShowWithGenreSlugsAndPersonalRating(
        genreSlugs = genreSlugs,
        personalRating = personalRating,
        screenplay = screenplay
    )
}

class MovieWithGenreSlugsAndPersonalRating(
    override val genreSlugs: Nel<GenreSlug>,
    override val personalRating: Rating,
    override val screenplay: Movie
) : ScreenplayWithGenreSlugsAndPersonalRating

class TvShowWithGenreSlugsAndPersonalRating(
    override val genreSlugs: Nel<GenreSlug>,
    override val personalRating: Rating,
    override val screenplay: TvShow
) : ScreenplayWithGenreSlugsAndPersonalRating

fun List<ScreenplayWithGenreSlugsAndPersonalRating>.ids(): List<TmdbScreenplayId> =
    map { it.screenplay.tmdbId }
