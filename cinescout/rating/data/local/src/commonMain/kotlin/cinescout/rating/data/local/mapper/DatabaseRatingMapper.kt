package cinescout.rating.data.local.mapper

import arrow.core.Nel
import cinescout.database.model.DatabaseMovieWithPersonalRating
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.database.model.DatabaseTvShowWithPersonalRating
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import com.soywiz.klock.Date
import org.koin.core.annotation.Factory

@Factory
class DatabaseRatingMapper(
    private val databaseScreenplayMapper: DatabaseScreenplayMapper
) {

    fun toScreenplayWithPersonalRating(
        tmdbId: Long,
        movieId: DatabaseTmdbMovieId?,
        tvShowId: DatabaseTmdbTvShowId?,
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
            movieId = movieId,
            overview = overview,
            ratingCount = ratingCount,
            ratingAverage = ratingAverage,
            releaseDate = releaseDate,
            title = title,
            tmdbId = tmdbId,
            tvShowId = tvShowId
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
                movieId = entry.tmdbId,
                overview = entry.overview,
                ratingCount = entry.ratingCount,
                ratingAverage = entry.ratingAverage,
                releaseDate = entry.releaseDate,
                title = entry.title,
                tmdbId = entry.tmdbId.value.toLong(),
                tvShowId = null
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
                movieId = null,
                overview = entry.overview,
                ratingCount = entry.ratingCount,
                ratingAverage = entry.ratingAverage,
                releaseDate = null,
                title = entry.title,
                tmdbId = entry.tmdbId.value.toLong(),
                tvShowId = entry.tmdbId
            ),
            personalRating = Rating.of(entry.personalRating).getOrThrow()
        )
    }
}
