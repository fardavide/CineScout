package cinescout.screenplay.data.local.mapper

import arrow.core.Option
import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseScreenplayType
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.database.model.DatabaseTvShow
import cinescout.database.model.getDataBaseScreenplayType
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.getOrThrow
import com.soywiz.klock.Date
import org.koin.core.annotation.Factory

@Factory
class DatabaseScreenplayMapper {

    fun toScreenplay(
        @Suppress("UNUSED_PARAMETER") tmdbId: Long,
        movieId: DatabaseTmdbMovieId?,
        tvShowId: DatabaseTmdbTvShowId?,
        firstAirDate: Date?,
        overview: String,
        ratingAverage: Double,
        ratingCount: Long,
        releaseDate: Date?,
        title: String
    ): Screenplay = when (getDataBaseScreenplayType(movieId, tvShowId)) {
        DatabaseScreenplayType.Movie -> toMovie(
            overview = overview,
            ratingCount = ratingCount,
            ratingAverage = ratingAverage,
            releaseDate = releaseDate,
            title = title,
            tmdbId = checkNotNull(movieId)
        )
        DatabaseScreenplayType.TvShow -> toTvShow(
            overview = overview,
            ratingCount = ratingCount,
            ratingAverage = ratingAverage,
            firstAirDate = checkNotNull(firstAirDate),
            title = title,
            tmdbId = checkNotNull(tvShowId)
        )
    }

    fun toDatabaseMovie(movie: Movie) = DatabaseMovie(
        tmdbId = movie.tmdbId.toDatabaseId(),
        overview = movie.overview,
        ratingAverage = movie.rating.average.value,
        ratingCount = movie.rating.voteCount.toLong(),
        releaseDate = movie.releaseDate.orNull(),
        title = movie.title
    )

    fun toDatabaseTvShow(tvShow: TvShow) = DatabaseTvShow(
        tmdbId = tvShow.tmdbId.toDatabaseId(),
        firstAirDate = tvShow.firstAirDate,
        overview = tvShow.overview,
        ratingAverage = tvShow.rating.average.value,
        ratingCount = tvShow.rating.voteCount.toLong(),
        title = tvShow.title
    )

    private fun toMovie(
        overview: String,
        ratingCount: Long,
        ratingAverage: Double,
        releaseDate: Date?,
        title: String,
        tmdbId: DatabaseTmdbMovieId
    ) = Movie(
        overview = overview,
        rating = PublicRating(
            voteCount = ratingCount.toInt(),
            average = Rating.of(ratingAverage).getOrThrow()
        ),
        releaseDate = Option.fromNullable(releaseDate),
        title = title,
        tmdbId = tmdbId.toMovieDomainId()
    )

    private fun toTvShow(
        overview: String,
        ratingCount: Long,
        ratingAverage: Double,
        firstAirDate: Date,
        title: String,
        tmdbId: DatabaseTmdbTvShowId
    ) = TvShow(
        overview = overview,
        rating = PublicRating(
            voteCount = ratingCount.toInt(),
            average = Rating.of(ratingAverage).getOrThrow()
        ),
        firstAirDate = firstAirDate,
        title = title,
        tmdbId = tmdbId.toTvShowDomainId()
    )
}