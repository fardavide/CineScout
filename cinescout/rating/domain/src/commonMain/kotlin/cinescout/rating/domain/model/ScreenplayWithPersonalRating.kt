package cinescout.rating.domain.model

import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.id.TmdbScreenplayId

sealed interface ScreenplayWithPersonalRating {

    val personalRating: Rating
    val screenplay: Screenplay
}

fun ScreenplayWithPersonalRating(
    personalRating: Rating,
    screenplay: Screenplay
): ScreenplayWithPersonalRating = when (screenplay) {
    is Movie -> MovieWithPersonalRating(
        personalRating = personalRating,
        screenplay = screenplay
    )
    is TvShow -> TvShowWithPersonalRating(
        personalRating = personalRating,
        screenplay = screenplay
    )
}

class MovieWithPersonalRating(
    override val personalRating: Rating,
    override val screenplay: Movie
) : ScreenplayWithPersonalRating

class TvShowWithPersonalRating(
    override val personalRating: Rating,
    override val screenplay: TvShow
) : ScreenplayWithPersonalRating

fun List<ScreenplayWithPersonalRating>.ids(): List<TmdbScreenplayId> = map { it.screenplay.tmdbId }
