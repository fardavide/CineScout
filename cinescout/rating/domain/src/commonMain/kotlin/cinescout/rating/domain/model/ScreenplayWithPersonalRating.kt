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
    screenplay: Screenplay,
    personalRating: Rating
): ScreenplayWithPersonalRating = when (screenplay) {
    is Movie -> MovieWithPersonalRating(screenplay, personalRating)
    is TvShow -> TvShowWithPersonalRating(screenplay, personalRating)
}

class MovieWithPersonalRating(
    override val screenplay: Movie,
    override val personalRating: Rating
) : ScreenplayWithPersonalRating

class TvShowWithPersonalRating(
    override val screenplay: TvShow,
    override val personalRating: Rating
) : ScreenplayWithPersonalRating

fun List<ScreenplayWithPersonalRating>.ids(): List<TmdbScreenplayId> = map { it.screenplay.tmdbId }
