package cinescout.screenplay.data.mapper

import arrow.core.Option
import arrow.core.toOption
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.TvShowStatus
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.model.ids.MovieIds
import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TmdbTvShowId
import cinescout.screenplay.domain.model.ids.TraktMovieId
import cinescout.screenplay.domain.model.ids.TraktTvShowId
import cinescout.screenplay.domain.model.ids.TvShowIds
import cinescout.utils.kotlin.takeIfNotBlank
import korlibs.time.Date
import org.koin.core.annotation.Factory
import kotlin.time.Duration

@Factory
class ScreenplayMapper {

    fun toMovie(
        overview: String,
        voteCount: Int,
        voteAverage: Double,
        releaseDate: Date?,
        runtime: Duration?,
        tagline: String?,
        title: String,
        tmdbId: TmdbMovieId,
        traktId: TraktMovieId
    ) = Movie(
        ids = MovieIds(
            tmdb = tmdbId,
            trakt = traktId
        ),
        overview = overview,
        rating = PublicRating(
            voteCount = voteCount,
            average = Rating.of(voteAverage).getOrThrow()
        ),
        releaseDate = Option.fromNullable(releaseDate),
        runtime = Option.fromNullable(runtime),
        tagline = tagline?.takeIfNotBlank().toOption(),
        title = title
    )

    fun toTvShow(
        airedEpisodes: Int,
        firstAirDate: Date,
        overview: String,
        runtime: Duration?,
        status: TvShowStatus,
        voteCount: Int,
        voteAverage: Double,
        title: String,
        tmdbId: TmdbTvShowId,
        traktId: TraktTvShowId
    ) = TvShow(
        airedEpisodes = airedEpisodes,
        firstAirDate = firstAirDate,
        ids = TvShowIds(
            tmdb = tmdbId,
            trakt = traktId
        ),
        overview = overview,
        rating = PublicRating(
            voteCount = voteCount,
            average = Rating.of(voteAverage).getOrThrow()
        ),
        runtime = Option.fromNullable(runtime),
        status = status,
        title = title
    )
}
