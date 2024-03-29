package cinescout.rating.domain.model

import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.id.MovieIds
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import cinescout.screenplay.domain.model.id.TvShowIds

sealed interface ScreenplayIdWithPersonalRating {

    val personalRating: Rating
    val screenplayIds: ScreenplayIds
}

fun ScreenplayIdWithPersonalRating(
    screenplayIds: ScreenplayIds,
    personalRating: Rating
): ScreenplayIdWithPersonalRating = when (screenplayIds) {
    is MovieIds -> MovieIdWithPersonalRating(screenplayIds, personalRating)
    is TvShowIds -> TvShowIdWithPersonalRating(screenplayIds, personalRating)
}

class MovieIdWithPersonalRating(
    override val screenplayIds: MovieIds,
    override val personalRating: Rating
) : ScreenplayIdWithPersonalRating

class TvShowIdWithPersonalRating(
    override val screenplayIds: TvShowIds,
    override val personalRating: Rating
) : ScreenplayIdWithPersonalRating

fun List<ScreenplayIdWithPersonalRating>.ids(): List<ScreenplayIds> = map { it.screenplayIds }

fun List<ScreenplayIdWithPersonalRating>.tmdbIds(): List<TmdbScreenplayId> = map { it.screenplayIds.tmdb }
