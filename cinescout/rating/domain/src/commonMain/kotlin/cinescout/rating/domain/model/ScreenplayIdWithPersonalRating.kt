package cinescout.rating.domain.model

import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbScreenplayId

sealed interface ScreenplayIdWithPersonalRating {

    val personalRating: Rating
    val screenplayId: TmdbScreenplayId
}

fun ScreenplayIdWithPersonalRating(
    screenplayId: TmdbScreenplayId,
    personalRating: Rating
): ScreenplayIdWithPersonalRating = when (screenplayId) {
    is TmdbScreenplayId.Movie -> MovieIdWithPersonalRating(screenplayId, personalRating)
    is TmdbScreenplayId.TvShow -> TvShowIdWithPersonalRating(screenplayId, personalRating)
}

class MovieIdWithPersonalRating(
    override val screenplayId: TmdbScreenplayId.Movie,
    override val personalRating: Rating
) : ScreenplayIdWithPersonalRating

class TvShowIdWithPersonalRating(
    override val screenplayId: TmdbScreenplayId.TvShow,
    override val personalRating: Rating
) : ScreenplayIdWithPersonalRating

fun List<ScreenplayIdWithPersonalRating>.ids(): List<TmdbScreenplayId> = map { it.screenplayId }
