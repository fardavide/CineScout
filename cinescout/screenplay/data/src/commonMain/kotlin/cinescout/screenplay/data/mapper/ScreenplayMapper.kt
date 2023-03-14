package cinescout.screenplay.data.mapper

import arrow.core.Option
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.getOrThrow
import com.soywiz.klock.Date
import org.koin.core.annotation.Factory

@Factory
class ScreenplayMapper {

    fun toMovie(
        overview: String,
        voteCount: Int,
        voteAverage: Double,
        releaseDate: Date?,
        title: String,
        tmdbId: TmdbScreenplayId.Movie
    ) = Movie(
        overview = overview,
        rating = PublicRating(
            voteCount = voteCount,
            average = Rating.of(voteAverage).getOrThrow()
        ),
        releaseDate = Option.fromNullable(releaseDate),
        title = title,
        tmdbId = tmdbId
    )

    fun toTvShow(
        firstAirDate: Date,
        overview: String,
        voteCount: Int,
        voteAverage: Double,
        title: String,
        tmdbId: TmdbScreenplayId.TvShow
    ) = TvShow(
        firstAirDate = firstAirDate,
        overview = overview,
        rating = PublicRating(
            voteCount = voteCount,
            average = Rating.of(voteAverage).getOrThrow()
        ),
        title = title,
        tmdbId = tmdbId
    )
}
