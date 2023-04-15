package cinescout.screenplay.data.remote.tmdb.mapper

import arrow.core.toOption
import cinescout.screenplay.data.remote.tmdb.model.GetMovieResponse
import cinescout.screenplay.data.remote.tmdb.model.GetTvShowResponse
import cinescout.screenplay.data.remote.tmdb.model.TmdbMovie
import cinescout.screenplay.data.remote.tmdb.model.TmdbTvShow
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.getOrThrow
import com.soywiz.klock.Date
import org.koin.core.annotation.Factory

@Factory
class TmdbScreenplayMapper {

    fun toMovie(tmdbMovie: TmdbMovie) = toMovie(
        overview = tmdbMovie.overview,
        releaseDate = tmdbMovie.releaseDate,
        title = tmdbMovie.title,
        tmdbId = tmdbMovie.id,
        voteAverage = tmdbMovie.voteAverage,
        voteCount = tmdbMovie.voteCount
    )

    fun toTvShow(tmdbTvShow: TmdbTvShow) = toTvShow(
        firstAirDate = tmdbTvShow.firstAirDate,
        overview = tmdbTvShow.overview,
        voteAverage = tmdbTvShow.voteAverage,
        voteCount = tmdbTvShow.voteCount,
        title = tmdbTvShow.title,
        tmdbId = tmdbTvShow.id
    )

    fun toMovie(response: GetMovieResponse) = Movie(
        overview = response.overview,
        rating = PublicRating(
            voteCount = response.voteCount,
            average = Rating.of(response.voteAverage).getOrThrow()
        ),
        releaseDate = response.releaseDate.toOption(),
        title = response.title,
        tmdbId = response.id,
        traktId = TraktScreenplayId.Movie(0) // TODO: Implement
    )

    fun toTvShow(response: GetTvShowResponse) = TvShow(
        firstAirDate = response.firstAirDate,
        overview = response.overview,
        rating = PublicRating(
            voteCount = response.voteCount,
            average = Rating.of(response.voteAverage).getOrThrow()
        ),
        title = response.name,
        tmdbId = response.id,
        traktId = TraktScreenplayId.TvShow(0) // TODO: Implement
    )

    fun toMovies(tmdbMovies: List<TmdbMovie>): List<Movie> = tmdbMovies.map(::toMovie)

    fun toTvShows(tmdbTvShows: List<TmdbTvShow>): List<TvShow> = tmdbTvShows.map(::toTvShow)

    private fun toMovie(
        overview: String,
        releaseDate: Date?,
        title: String,
        tmdbId: TmdbScreenplayId.Movie,
        voteAverage: Double,
        voteCount: Int
    ) = Movie(
        overview = overview,
        rating = PublicRating(
            voteCount = voteCount,
            average = Rating.of(voteAverage).getOrThrow()
        ),
        releaseDate = releaseDate.toOption(),
        title = title,
        tmdbId = tmdbId,
        traktId = TraktScreenplayId.Movie(0) // TODO: Implement
    )

    private fun toTvShow(
        firstAirDate: Date,
        overview: String,
        title: String,
        tmdbId: TmdbScreenplayId.TvShow,
        voteAverage: Double,
        voteCount: Int
    ) = TvShow(
        firstAirDate = firstAirDate,
        overview = overview,
        rating = PublicRating(
            voteCount = voteCount,
            average = Rating.of(voteAverage).getOrThrow()
        ),
        title = title,
        tmdbId = tmdbId,
        traktId = TraktScreenplayId.TvShow(0) // TODO: Implement
    )
}
