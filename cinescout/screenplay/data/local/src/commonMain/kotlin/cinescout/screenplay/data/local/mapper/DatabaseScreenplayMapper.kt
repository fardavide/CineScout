package cinescout.screenplay.data.local.mapper

import arrow.core.Option
import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseScreenplayType
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.database.model.DatabaseTraktMovieId
import cinescout.database.model.DatabaseTraktTvShowId
import cinescout.database.model.DatabaseTvShow
import cinescout.database.model.getDataBaseScreenplayType
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.getOrThrow
import korlibs.time.Date
import org.koin.core.annotation.Factory

@Factory
class DatabaseScreenplayMapper {

    fun toScreenplay(
        tmdbMovieId: DatabaseTmdbMovieId?,
        traktMovieId: DatabaseTraktMovieId?,
        tmdbTvShowId: DatabaseTmdbTvShowId?,
        traktTvShowId: DatabaseTraktTvShowId?,
        firstAirDate: Date?,
        overview: String,
        ratingAverage: Double,
        ratingCount: Long,
        releaseDate: Date?,
        title: String
    ): Screenplay = when (getDataBaseScreenplayType(tmdbMovieId, tmdbTvShowId)) {
        DatabaseScreenplayType.Movie -> toMovie(
            overview = overview,
            ratingCount = ratingCount,
            ratingAverage = ratingAverage,
            releaseDate = releaseDate,
            title = title,
            tmdbId = checkNotNull(tmdbMovieId),
            traktId = checkNotNull(traktMovieId)
        )
        DatabaseScreenplayType.TvShow -> toTvShow(
            overview = overview,
            ratingCount = ratingCount,
            ratingAverage = ratingAverage,
            firstAirDate = checkNotNull(firstAirDate),
            title = title,
            tmdbId = checkNotNull(tmdbTvShowId),
            traktId = checkNotNull(traktTvShowId)
        )
    }

    fun toDatabaseMovie(movie: Movie) = DatabaseMovie(
        overview = movie.overview,
        ratingAverage = movie.rating.average.value,
        ratingCount = movie.rating.voteCount.toLong(),
        releaseDate = movie.releaseDate.orNull(),
        title = movie.title,
        tmdbId = movie.tmdbId.toDatabaseId(),
        traktId = movie.traktId.toDatabaseId()
    )

    fun toDatabaseTvShow(tvShow: TvShow) = DatabaseTvShow(
        firstAirDate = tvShow.firstAirDate,
        overview = tvShow.overview,
        ratingAverage = tvShow.rating.average.value,
        ratingCount = tvShow.rating.voteCount.toLong(),
        title = tvShow.title,
        tmdbId = tvShow.tmdbId.toDatabaseId(),
        traktId = tvShow.traktId.toDatabaseId()
    )

    private fun toMovie(
        overview: String,
        ratingCount: Long,
        ratingAverage: Double,
        releaseDate: Date?,
        title: String,
        tmdbId: DatabaseTmdbMovieId,
        traktId: DatabaseTraktMovieId
    ) = Movie(
        ids = ScreenplayIds.Movie(
            tmdb = tmdbId.toMovieDomainId(),
            trakt = traktId.toMovieDomainId()
        ),
        overview = overview,
        rating = PublicRating(
            voteCount = ratingCount.toInt(),
            average = Rating.of(ratingAverage).getOrThrow()
        ),
        releaseDate = Option.fromNullable(releaseDate),
        title = title
    )

    private fun toTvShow(
        overview: String,
        ratingCount: Long,
        ratingAverage: Double,
        firstAirDate: Date,
        title: String,
        tmdbId: DatabaseTmdbTvShowId,
        traktId: DatabaseTraktTvShowId
    ) = TvShow(
        firstAirDate = firstAirDate,
        ids = ScreenplayIds.TvShow(
            tmdb = tmdbId.toTvShowDomainId(),
            trakt = traktId.toTvShowDomainId()
        ),
        overview = overview,
        rating = PublicRating(
            voteCount = ratingCount.toInt(),
            average = Rating.of(ratingAverage).getOrThrow()
        ),
        title = title
    )
}
