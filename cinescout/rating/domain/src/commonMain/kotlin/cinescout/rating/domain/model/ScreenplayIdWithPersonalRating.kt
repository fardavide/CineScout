package cinescout.rating.domain.model

import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.TmdbScreenplayId

sealed interface ScreenplayIdWithPersonalRating {

    val personalRating: Rating
    val screenplayIds: ScreenplayIds
}

fun ScreenplayIdWithPersonalRating(
    screenplayIds: ScreenplayIds,
    personalRating: Rating
): ScreenplayIdWithPersonalRating = when (screenplayIds) {
    is ScreenplayIds.Movie -> MovieIdWithPersonalRating(screenplayIds, personalRating)
    is ScreenplayIds.TvShow -> TvShowIdWithPersonalRating(screenplayIds, personalRating)
}

class MovieIdWithPersonalRating(
    override val screenplayIds: ScreenplayIds.Movie,
    override val personalRating: Rating
) : ScreenplayIdWithPersonalRating

class TvShowIdWithPersonalRating(
    override val screenplayIds: ScreenplayIds.TvShow,
    override val personalRating: Rating
) : ScreenplayIdWithPersonalRating

fun List<ScreenplayIdWithPersonalRating>.ids(): List<ScreenplayIds> = map { it.screenplayIds }

fun List<ScreenplayIdWithPersonalRating>.tmdbIds(): List<TmdbScreenplayId> = map { it.screenplayIds.tmdb }
