package cinescout.watchlist.data.local

import arrow.core.Option
import cinescout.database.model.DatabaseScreenplayType
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.database.model.getDataBaseScreenplayType
import cinescout.screenplay.data.local.mapper.toMovieDomainId
import cinescout.screenplay.data.local.mapper.toTvShowDomainId
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.getOrThrow
import com.soywiz.klock.Date

class DatabaseScreenplayMapper {

    @Suppress("UNUSED_PARAMETER", "LongParameterList")
    fun toScreenplay(
        tmdbId: Long,
        movieId: DatabaseTmdbMovieId?,
        tvShowId: DatabaseTmdbTvShowId?,
        backdropPath: String?,
        firstAirDate: Date?,
        overview: String,
        posterPath: String?,
        ratingAverage: Double,
        ratingCount: Long,
        releaseDate: Date?,
        title: String
    ): Screenplay = when (getDataBaseScreenplayType(movieId, tvShowId)) {
        DatabaseScreenplayType.Movie -> toMovie(
            backdropPath = backdropPath,
            overview = overview,
            posterPath = posterPath,
            ratingCount = ratingCount,
            ratingAverage = ratingAverage,
            releaseDate = releaseDate,
            title = title,
            tmdbId = checkNotNull(movieId)
        )
        DatabaseScreenplayType.TvShow -> toTvShow(
            backdropPath = backdropPath,
            overview = overview,
            posterPath = posterPath,
            ratingCount = ratingCount,
            ratingAverage = ratingAverage,
            firstAirDate = checkNotNull(firstAirDate),
            title = title,
            tmdbId = checkNotNull(tvShowId)
        )
    }

    @Suppress("LongParameterList")
    fun toMovie(
        backdropPath: String?,
        overview: String,
        posterPath: String?,
        ratingCount: Long,
        ratingAverage: Double,
        releaseDate: Date?,
        title: String,
        tmdbId: DatabaseTmdbMovieId
    ) = Movie(
        backdropImage = Option.fromNullable(backdropPath).map(::TmdbBackdropImage),
        overview = overview,
        posterImage = Option.fromNullable(posterPath).map(::TmdbPosterImage),
        rating = PublicRating(
            voteCount = ratingCount.toInt(),
            average = Rating.of(ratingAverage).getOrThrow()
        ),
        releaseDate = Option.fromNullable(releaseDate),
        title = title,
        tmdbId = tmdbId.toMovieDomainId()
    )

    @Suppress("LongParameterList")
    fun toTvShow(
        backdropPath: String?,
        overview: String,
        posterPath: String?,
        ratingCount: Long,
        ratingAverage: Double,
        firstAirDate: Date,
        title: String,
        tmdbId: DatabaseTmdbTvShowId
    ) = TvShow(
        backdropImage = Option.fromNullable(backdropPath).map(::TmdbBackdropImage),
        overview = overview,
        posterImage = Option.fromNullable(posterPath).map(::TmdbPosterImage),
        rating = PublicRating(
            voteCount = ratingCount.toInt(),
            average = Rating.of(ratingAverage).getOrThrow()
        ),
        firstAirDate = firstAirDate,
        title = title,
        tmdbId = tmdbId.toTvShowDomainId()
    )
}
