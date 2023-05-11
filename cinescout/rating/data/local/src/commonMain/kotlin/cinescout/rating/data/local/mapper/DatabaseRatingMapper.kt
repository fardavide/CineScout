package cinescout.rating.data.local.mapper

import arrow.core.Nel
import cinescout.database.model.DatabaseMovieWithPersonalRating
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.database.model.DatabaseTraktMovieId
import cinescout.database.model.DatabaseTraktTvShowId
import cinescout.database.model.DatabaseTvShowWithPersonalRating
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import korlibs.time.Date
import org.koin.core.annotation.Factory

@Factory
class DatabaseRatingMapper(
    private val databaseScreenplayMapper: DatabaseScreenplayMapper
) {

    fun toScreenplayWithPersonalRating(
        tmdbMovieId: DatabaseTmdbMovieId?,
        traktMovieId: DatabaseTraktMovieId?,
        tmdbTvShowId: DatabaseTmdbTvShowId?,
        traktTvShowId: DatabaseTraktTvShowId?,
        firstAirDate: Date?,
        overview: String,
        ratingAverage: Double,
        ratingCount: Long,
        releaseDate: Date?,
        title: String,
        personalRating: Int
    ) = ScreenplayWithPersonalRating(
        screenplay = databaseScreenplayMapper.toScreenplay(
            firstAirDate = firstAirDate,
            tmdbMovieId = tmdbMovieId,
            traktMovieId = traktMovieId,
            overview = overview,
            ratingCount = ratingCount,
            ratingAverage = ratingAverage,
            releaseDate = releaseDate,
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
                firstAirDate = null,
                tmdbMovieId = entry.tmdbId,
                traktMovieId = entry.traktId,
                overview = entry.overview,
                ratingCount = entry.ratingCount,
                ratingAverage = entry.ratingAverage,
                releaseDate = entry.releaseDate,
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
                firstAirDate = entry.firstAirDate,
                tmdbMovieId = null,
                traktMovieId = null,
                overview = entry.overview,
                ratingCount = entry.ratingCount,
                ratingAverage = entry.ratingAverage,
                releaseDate = null,
                title = entry.title,
                tmdbTvShowId = entry.tmdbId,
                traktTvShowId = entry.traktId
            ),
            personalRating = Rating.of(entry.personalRating).getOrThrow()
        )
    }
}
