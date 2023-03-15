package cinescout.screenplay.data.remote.tmdb.mapper

import arrow.core.toOption
import cinescout.screenplay.data.remote.tmdb.model.GetMovieResponse
import cinescout.screenplay.data.remote.tmdb.model.GetTvShowResponse
import cinescout.screenplay.data.remote.tmdb.model.TmdbMovie
import cinescout.screenplay.data.remote.tmdb.model.TmdbTvShow
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.getOrThrow
import org.koin.core.annotation.Factory

@Factory
internal class TmdbScreenplayMapper {

    fun toMovie(tmdbMovie: TmdbMovie) = Movie(
        overview = tmdbMovie.overview,
        rating = PublicRating(
            voteCount = tmdbMovie.voteCount,
            average = Rating.of(tmdbMovie.voteAverage).getOrThrow()
        ),
        releaseDate = tmdbMovie.releaseDate.toOption(),
        title = tmdbMovie.title,
        tmdbId = tmdbMovie.id
    )

    fun toTvShow(tmdbTvShow: TmdbTvShow) = TvShow(
        firstAirDate = tmdbTvShow.firstAirDate,
        overview = tmdbTvShow.overview,
        rating = PublicRating(
            voteCount = tmdbTvShow.voteCount,
            average = Rating.of(tmdbTvShow.voteAverage).getOrThrow()
        ),
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
        tmdbId = response.id
    )

    fun toTvShow(response: GetTvShowResponse) = TvShow(
        firstAirDate = response.firstAirDate,
        overview = response.overview,
        rating = PublicRating(
            voteCount = response.voteCount,
            average = Rating.of(response.voteAverage).getOrThrow()
        ),
        title = response.name,
        tmdbId = response.id
    )

    fun toMovies(tmdbMovies: List<TmdbMovie>): List<Movie> = tmdbMovies.map(::toMovie)

    fun toTvShows(tmdbTvShows: List<TmdbTvShow>): List<TvShow> = tmdbTvShows.map(::toTvShow)
}
