package cinescout.tvshows.domain.model

import cinescout.screenplay.domain.model.Rating
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class TvShowWithPersonalRating(
    val tvShow: TvShow,
    val personalRating: Rating
)

fun List<TvShowWithPersonalRating>.ids(): List<TvShowIdWithPersonalRating> =
    map { TvShowIdWithPersonalRating(it.tvShow.tmdbId, it.personalRating) }

fun Flow<List<TvShowWithPersonalRating>>.ids(): Flow<List<TvShowIdWithPersonalRating>> = map { it.ids() }
