package cinescout.tvshows.data.remote.tmdb.mapper

import arrow.core.Option
import arrow.core.valueOr
import cinescout.common.model.Genre
import cinescout.common.model.PublicRating
import cinescout.common.model.Rating
import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbGenreId
import cinescout.common.model.TmdbPosterImage
import cinescout.common.model.getOrThrow
import cinescout.tvshows.data.remote.tmdb.model.GetRatedTvShows
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowDetails
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowWatchlist
import cinescout.tvshows.data.remote.tmdb.model.TmdbTvShow
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowWithDetails
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import kotlin.math.roundToInt

class TmdbTvShowMapper {

    fun toTvShow(tmdbTvShow: TmdbTvShow) = TvShow(
        backdropImage = Option.fromNullable(tmdbTvShow.backdropPath).map(::TmdbBackdropImage),
        firstAirDate = tmdbTvShow.firstAirDate,
        overview = tmdbTvShow.overview,
        posterImage = Option.fromNullable(tmdbTvShow.posterPath).map(::TmdbPosterImage),
        rating = PublicRating(
            voteCount = tmdbTvShow.voteCount,
            average = Rating.of(tmdbTvShow.voteAverage).getOrThrow()
        ),
        title = tmdbTvShow.name,
        tmdbId = tmdbTvShow.id
    )

    fun toTvShowWithDetails(response: GetTvShowDetails.Response) = TvShowWithDetails(
        tvShow = toTvShow(response),
        genres = response.genres.map { genre -> Genre(id = TmdbGenreId(genre.id), name = genre.name) }
    )

    fun toTvShow(response: GetTvShowDetails.Response) = TvShow(
        backdropImage = Option.fromNullable(response.backdropPath).map(::TmdbBackdropImage),
        firstAirDate = response.firstAirDate,
        overview = response.overview,
        posterImage = Option.fromNullable(response.posterPath).map(::TmdbPosterImage),
        rating = PublicRating(
            voteCount = response.voteCount,
            average = Rating.of(response.voteAverage).getOrThrow()
        ),
        title = response.name,
        tmdbId = response.id
    )

    fun toTvShows(tmdbTvShows: List<TmdbTvShow>): List<TvShow> =
        tmdbTvShows.map(::toTvShow)

    fun toTvShows(response: GetTvShowWatchlist.Response): List<TvShow> {
        return response.results.map { pageResult ->
            toTvShow(pageResult.toTmdbTvShow())
        }
    }

    fun toTvShowsWithRating(response: GetRatedTvShows.Response): List<TvShowWithPersonalRating> {
        return response.results.map { pageResult ->
            TvShowWithPersonalRating(
                tvShow = toTvShow(pageResult.toTmdbTvShow()),
                personalRating = Rating.of(pageResult.rating.roundToInt())
                    .valueOr { throw IllegalStateException("Invalid rating: $it") }
            )
        }
    }
}
