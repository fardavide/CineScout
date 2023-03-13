package cinescout.screenplay.data.mapper

import arrow.core.Option
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.getOrThrow
import com.soywiz.klock.Date
import org.koin.core.annotation.Factory

@Factory
class ScreenplayMapper {

    fun toMovie(
        backdropPath: String?,
        overview: String,
        posterPath: String?,
        voteCount: Int,
        voteAverage: Double,
        releaseDate: Date?,
        title: String,
        tmdbId: TmdbScreenplayId.Movie
    ) = Movie(
        backdropImage = Option.fromNullable(backdropPath).map(::TmdbBackdropImage),
        overview = overview,
        posterImage = Option.fromNullable(posterPath).map(::TmdbPosterImage),
        rating = PublicRating(
            voteCount = voteCount,
            average = Rating.of(voteAverage).getOrThrow()
        ),
        releaseDate = Option.fromNullable(releaseDate),
        title = title,
        tmdbId = tmdbId
    )

    fun toTvShow(
        backdropPath: String?,
        firstAirDate: Date,
        overview: String,
        posterPath: String?,
        voteCount: Int,
        voteAverage: Double,
        title: String,
        tmdbId: TmdbScreenplayId.TvShow
    ) = TvShow(
        backdropImage = Option.fromNullable(backdropPath).map(::TmdbBackdropImage),
        firstAirDate = firstAirDate,
        overview = overview,
        posterImage = Option.fromNullable(posterPath).map(::TmdbPosterImage),
        rating = PublicRating(
            voteCount = voteCount,
            average = Rating.of(voteAverage).getOrThrow()
        ),
        title = title,
        tmdbId = tmdbId
    )
}
