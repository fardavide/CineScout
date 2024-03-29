package cinescout.screenplay.data.local.mapper

import arrow.core.Option
import arrow.core.toOption
import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseScreenplayType
import cinescout.database.model.DatabaseScreenplayWithGenreSlug
import cinescout.database.model.DatabaseTvShow
import cinescout.database.model.DatabaseTvShowStatus
import cinescout.database.model.getDataBaseScreenplayType
import cinescout.database.model.id.DatabaseTmdbMovieId
import cinescout.database.model.id.DatabaseTmdbTvShowId
import cinescout.database.model.id.DatabaseTraktMovieId
import cinescout.database.model.id.DatabaseTraktTvShowId
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayWithGenreSlugs
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.model.id.GenreSlug
import cinescout.screenplay.domain.model.id.MovieIds
import cinescout.screenplay.domain.model.id.TvShowIds
import cinescout.utils.kotlin.nonEmptyUnsafe
import korlibs.time.Date
import org.koin.core.annotation.Factory
import kotlin.time.Duration

@Factory
class DatabaseScreenplayMapper {

    fun toScreenplay(
        tmdbMovieId: DatabaseTmdbMovieId?,
        traktMovieId: DatabaseTraktMovieId?,
        tmdbTvShowId: DatabaseTmdbTvShowId?,
        traktTvShowId: DatabaseTraktTvShowId?,
        airedEpisodes: Long?,
        firstAirDate: Date?,
        overview: String,
        ratingAverage: Double,
        ratingCount: Long,
        releaseDate: Date?,
        runtime: Duration?,
        status: DatabaseTvShowStatus?,
        tagline: String?,
        title: String
    ): Screenplay = when (getDataBaseScreenplayType(tmdbMovieId, tmdbTvShowId)) {
        DatabaseScreenplayType.Movie -> toMovie(
            overview = overview,
            ratingCount = ratingCount,
            ratingAverage = ratingAverage,
            releaseDate = releaseDate,
            runtime = runtime,
            tagline = tagline,
            title = title,
            tmdbId = checkNotNull(tmdbMovieId),
            traktId = checkNotNull(traktMovieId)
        )
        DatabaseScreenplayType.TvShow -> toTvShow(
            airedEpisodes = checkNotNull(airedEpisodes).toInt(),
            firstAirDate = checkNotNull(firstAirDate),
            overview = overview,
            ratingCount = ratingCount,
            ratingAverage = ratingAverage,
            runtime = runtime,
            status = checkNotNull(status),
            title = title,
            tmdbId = checkNotNull(tmdbTvShowId),
            traktId = checkNotNull(traktTvShowId)
        )
    }

    fun toScreenplayWithGenreSlugs(
        entries: List<DatabaseScreenplayWithGenreSlug>
    ): ScreenplayWithGenreSlugs? {
        val screenplay = run {
            val entry = entries.firstOrNull() ?: return null
            toScreenplay(
                tmdbMovieId = entry.movieTmdbId,
                traktMovieId = entry.movieTraktId,
                tmdbTvShowId = entry.tvShowTmdbId,
                traktTvShowId = entry.tvShowTraktId,
                airedEpisodes = entry.airedEpisodes,
                firstAirDate = entry.firstAirDate,
                overview = entry.overview,
                ratingAverage = entry.ratingAverage,
                ratingCount = entry.ratingCount,
                releaseDate = entry.releaseDate,
                runtime = entry.runtime,
                status = entry.status,
                tagline = entry.tagline,
                title = entry.title
            )
        }
        return ScreenplayWithGenreSlugs(
            screenplay = screenplay,
            genreSlugs = entries.nonEmptyUnsafe().map { GenreSlug(it.genreSlug.value) }
        )
    }

    fun toDatabaseMovie(movie: Movie) = DatabaseMovie(
        overview = movie.overview,
        ratingAverage = movie.rating.average.value,
        ratingCount = movie.rating.voteCount.toLong(),
        releaseDate = movie.releaseDate.getOrNull(),
        runtime = movie.runtime.getOrNull(),
        tagline = movie.tagline.getOrNull(),
        title = movie.title,
        tmdbId = movie.tmdbId.toDatabaseId(),
        traktId = movie.traktId.toDatabaseId()
    )

    fun toDatabaseTvShow(tvShow: TvShow) = DatabaseTvShow(
        airedEpisodes = tvShow.airedEpisodes.toLong(),
        firstAirDate = tvShow.firstAirDate,
        overview = tvShow.overview,
        ratingAverage = tvShow.rating.average.value,
        ratingCount = tvShow.rating.voteCount.toLong(),
        runtime = tvShow.runtime.getOrNull(),
        status = tvShow.status.toDatabaseStatus(),
        title = tvShow.title,
        tmdbId = tvShow.tmdbId.toDatabaseId(),
        traktId = tvShow.traktId.toDatabaseId()
    )

    private fun toMovie(
        overview: String,
        ratingCount: Long,
        ratingAverage: Double,
        releaseDate: Date?,
        runtime: Duration?,
        tagline: String?,
        title: String,
        tmdbId: DatabaseTmdbMovieId,
        traktId: DatabaseTraktMovieId
    ) = Movie(
        ids = MovieIds(
            tmdb = tmdbId.toMovieDomainId(),
            trakt = traktId.toMovieDomainId()
        ),
        overview = overview,
        rating = PublicRating(
            voteCount = ratingCount.toInt(),
            average = Rating.of(ratingAverage).getOrThrow()
        ),
        releaseDate = Option.fromNullable(releaseDate),
        runtime = Option.fromNullable(runtime),
        tagline = tagline.toOption(),
        title = title
    )

    private fun toTvShow(
        airedEpisodes: Int,
        firstAirDate: Date,
        overview: String,
        ratingCount: Long,
        ratingAverage: Double,
        runtime: Duration?,
        status: DatabaseTvShowStatus,
        title: String,
        tmdbId: DatabaseTmdbTvShowId,
        traktId: DatabaseTraktTvShowId
    ) = TvShow(
        airedEpisodes = airedEpisodes,
        firstAirDate = firstAirDate,
        ids = TvShowIds(
            tmdb = tmdbId.toTvShowDomainId(),
            trakt = traktId.toTvShowDomainId()
        ),
        overview = overview,
        rating = PublicRating(
            voteCount = ratingCount.toInt(),
            average = Rating.of(ratingAverage).getOrThrow()
        ),
        runtime = Option.fromNullable(runtime),
        status = status.toTvShowStatus(),
        title = title
    )
}
