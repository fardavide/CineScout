package cinescout.tvshows.data.local.mapper

import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.database.model.DatabaseTvShow
import cinescout.database.model.DatabaseTvShowWithPersonalRating
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import com.soywiz.klock.Date
import org.koin.core.annotation.Factory

@Factory
class DatabaseTvShowMapper {

    fun toTvShow(
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
        tmdbId = tmdbId.toId()
    )

    fun toTvShow(databaseTvShow: DatabaseTvShow) = toTvShow(
        overview = databaseTvShow.overview,
        ratingCount = databaseTvShow.ratingCount,
        ratingAverage = databaseTvShow.ratingAverage,
        firstAirDate = databaseTvShow.firstAirDate,
        title = databaseTvShow.title,
        tmdbId = databaseTvShow.tmdbId
    )

    fun toTvShowsWithRating(list: List<DatabaseTvShowWithPersonalRating>): List<TvShowWithPersonalRating> =
        list.map { entry ->
            val rating = Rating.of(entry.personalRating).getOrThrow()

            TvShowWithPersonalRating(
                tvShow = toTvShow(
                    overview = entry.overview,
                    ratingCount = entry.ratingCount,
                    ratingAverage = entry.ratingAverage,
                    firstAirDate = entry.firstAirDate,
                    title = entry.title,
                    tmdbId = entry.tmdbId
                ),
                personalRating = rating
            )
        }

    fun toDatabaseTvShow(tvShow: TvShow) = DatabaseTvShow(
        overview = tvShow.overview,
        ratingCount = tvShow.rating.voteCount.toLong(),
        ratingAverage = tvShow.rating.average.value,
        firstAirDate = tvShow.firstAirDate,
        title = tvShow.title,
        tmdbId = tvShow.tmdbId.toDatabaseId()
    )
}
