package cinescout.tvshows.data.local.mapper

import arrow.core.NonEmptyList
import arrow.core.Option
import arrow.core.valueOr
import cinescout.common.model.PublicRating
import cinescout.common.model.Rating
import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage
import cinescout.common.model.getOrThrow
import cinescout.database.model.DatabaseTvShow
import cinescout.database.model.DatabaseTvShowWithPersonalRating
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowWithPersonalRating

internal class DatabaseTvShowMapper {

    fun toTvShow(databaseTvShow: DatabaseTvShow) = TvShow(
        backdropImage = Option.fromNullable(databaseTvShow.backdropPath).map(::TmdbBackdropImage),
        firstAirDate = databaseTvShow.firstAirDate,
        overview = databaseTvShow.overview,
        posterImage = Option.fromNullable(databaseTvShow.posterPath).map(::TmdbPosterImage),
        rating = PublicRating(
            voteCount = databaseTvShow.ratingCount.toInt(),
            average = Rating.of(databaseTvShow.ratingAverage).getOrThrow()
        ),
        title = databaseTvShow.title,
        tmdbId = databaseTvShow.tmdbId.toId()
    )

    fun toTvShowsWithRating(
        list: List<DatabaseTvShowWithPersonalRating>
    ): List<TvShowWithPersonalRating> = list.map { entry ->
        val rating = Rating.of(entry.personalRating).getOrThrow()

        TvShowWithPersonalRating(
            tvShow = TvShow(
                backdropImage = Option.fromNullable(entry.backdropPath).map(::TmdbBackdropImage),
                firstAirDate = entry.firstAirDate,
                overview = entry.overview,
                posterImage = Option.fromNullable(entry.posterPath).map(::TmdbPosterImage),
                rating = PublicRating(
                    voteCount = entry.ratingCount.toInt(),
                    average = Rating.of(entry.ratingAverage).getOrThrow()
                ),
                tmdbId = entry.tmdbId.toId(),
                title = entry.title
            ),
            personalRating = rating
        )
    }

    fun toTvShowsWithRating(list: NonEmptyList<DatabaseTvShowWithPersonalRating>): NonEmptyList<TvShowWithPersonalRating> =
        list.map { entry ->
            val rating = Rating.of(entry.personalRating)
                .valueOr { throw IllegalStateException("Invalid rating: $it") }

            TvShowWithPersonalRating(
                tvShow = TvShow(
                    backdropImage = Option.fromNullable(entry.backdropPath).map(::TmdbBackdropImage),
                    firstAirDate = entry.firstAirDate,
                    overview = entry.overview,
                    posterImage = Option.fromNullable(entry.posterPath).map(::TmdbPosterImage),
                    rating = PublicRating(
                        voteCount = entry.ratingCount.toInt(),
                        average = Rating.of(entry.ratingAverage).getOrThrow()
                    ),
                    tmdbId = entry.tmdbId.toId(),
                    title = entry.title
                ),
                personalRating = rating
            )
        }
}
