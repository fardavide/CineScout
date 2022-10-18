package cinescout.tvshows.data.remote.trakt.mapper

import arrow.core.valueOr
import cinescout.common.model.Rating
import cinescout.tvshows.data.remote.model.TraktPersonalTvShowRating
import cinescout.tvshows.data.remote.trakt.model.GetRatings
import kotlin.math.roundToInt

internal class TraktTvShowMapper {

    fun toTvShowRating(tvShow: GetRatings.Result.TvShow) = TraktPersonalTvShowRating(
        tmdbId = tvShow.tvShow.ids.tmdb,
        rating = Rating.of(tvShow.rating.roundToInt())
            .valueOr { throw IllegalStateException("Invalid rating: $it") }
    )

    fun toTvShowRatings(movies: List<GetRatings.Result.TvShow>): List<TraktPersonalTvShowRating> =
        movies.map(::toTvShowRating)
}
