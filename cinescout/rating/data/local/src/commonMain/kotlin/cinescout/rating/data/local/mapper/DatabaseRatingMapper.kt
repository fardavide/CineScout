package cinescout.rating.data.local.mapper

import arrow.core.Nel
import cinescout.database.model.DatabaseMovieWithPersonalRating
import cinescout.database.model.DatabaseTvShowStatus
import cinescout.database.model.DatabaseTvShowWithPersonalRating
import cinescout.database.model.id.DatabaseTmdbMovieId
import cinescout.database.model.id.DatabaseTmdbTvShowId
import cinescout.database.model.id.DatabaseTraktMovieId
import cinescout.database.model.id.DatabaseTraktTvShowId
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import korlibs.time.Date
import org.koin.core.annotation.Factory
import kotlin.time.Duration

@Factory
class DatabaseRatingMapper(
    private val databaseScreenplayMapper: DatabaseScreenplayMapper
) {

    fun toScreenplayWithPersonalRating(
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
        title: String,
        personalRating: Int
    ) = ScreenplayWithPersonalRating(
        screenplay = databaseScreenplayMapper.toScreenplay(
            airedEpisodes = airedEpisodes,
            firstAirDate = firstAirDate,
            tmdbMovieId = tmdbMovieId,
            traktMovieId = traktMovieId,
            overview = overview,
            ratingCount = ratingCount,
            ratingAverage = ratingAverage,
            releaseDate = releaseDate,
            runtime = runtime,
            status = status,
            tagline = tagline,
            title = title,
            tmdbTvShowId = tmdbTvShowId,
            traktTvShowId = traktTvShowId
        ),
        personalRating = Rating.of(personalRating).getOrThrow()
    )
    
    @JvmName("toScreenplayWithPersonalRatingMovie")
    fun toScreenplayWithPersonalRating(
        list: Nel<DatabaseMovieWithPersonalRating>
    ): Nel<ScreenplayWithPersonalRating> = list.map { entry ->
        ScreenplayWithPersonalRating(
            screenplay = databaseScreenplayMapper.toScreenplay(
                airedEpisodes = null,
                firstAirDate = null,
                tmdbMovieId = entry.tmdbId,
                traktMovieId = entry.traktId,
                overview = entry.overview,
                ratingCount = entry.ratingCount,
                ratingAverage = entry.ratingAverage,
                releaseDate = entry.releaseDate,
                runtime = entry.runtime,
                status = null,
                tagline = entry.tagline,
                title = entry.title,
                tmdbTvShowId = null,
                traktTvShowId = null
            ),
            personalRating = Rating.of(entry.personalRating).getOrThrow()
        )
    }

    @JvmName("toScreenplayWithPersonalRatingTvShow")
    fun toScreenplayWithPersonalRating(
        list: Nel<DatabaseTvShowWithPersonalRating>
    ): Nel<ScreenplayWithPersonalRating> = list.map { entry ->
        ScreenplayWithPersonalRating(
            screenplay = databaseScreenplayMapper.toScreenplay(
                airedEpisodes = entry.airedEpisodes,
                firstAirDate = entry.firstAirDate,
                tmdbMovieId = null,
                traktMovieId = null,
                overview = entry.overview,
                ratingCount = entry.ratingCount,
                ratingAverage = entry.ratingAverage,
                releaseDate = null,
                runtime = entry.runtime,
                tagline = null,
                status = entry.status,
                title = entry.title,
                tmdbTvShowId = entry.tmdbId,
                traktTvShowId = entry.traktId
            ),
            personalRating = Rating.of(entry.personalRating).getOrThrow()
        )
    }
}
