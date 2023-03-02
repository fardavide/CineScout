package cinescout.tvshows.data.local.mapper

import arrow.core.Option
import cinescout.database.model.DatabaseTvShow
import cinescout.database.model.DatabaseTvShowWithPersonalRating
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import org.koin.core.annotation.Factory

@Factory
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

    fun toTvShowsWithRating(list: List<DatabaseTvShowWithPersonalRating>): List<TvShowWithPersonalRating> =
        list.map { entry ->
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

}
