package cinescout.movies.data.local.mapper

import arrow.core.NonEmptyList
import arrow.core.Option
import arrow.core.valueOr
import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseMovieWithPersonalRating
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import com.soywiz.klock.Date
import org.koin.core.annotation.Factory

@Factory
class DatabaseMovieMapper {

    @Suppress("LongParameterList")
    fun toMovie(
        overview: String,
        ratingCount: Long,
        ratingAverage: Double,
        releaseDate: Date?,
        title: String,
        tmdbId: DatabaseTmdbMovieId
    ) = Movie(
        overview = overview,
        rating = PublicRating(
            voteCount = ratingCount.toInt(),
            average = Rating.of(ratingAverage).getOrThrow()
        ),
        releaseDate = Option.fromNullable(releaseDate),
        title = title,
        tmdbId = tmdbId.toId()
    )

    fun toMovie(databaseMovie: DatabaseMovie): Movie = toMovie(
        overview = databaseMovie.overview,
        ratingCount = databaseMovie.ratingCount,
        ratingAverage = databaseMovie.ratingAverage,
        releaseDate = databaseMovie.releaseDate,
        title = databaseMovie.title,
        tmdbId = databaseMovie.tmdbId
    )

    fun toMoviesWithRating(
        list: NonEmptyList<DatabaseMovieWithPersonalRating>
    ): NonEmptyList<MovieWithPersonalRating> = list.map { entry ->
        val rating = Rating.of(entry.personalRating)
            .valueOr { throw IllegalStateException("Invalid rating: $it") }

        MovieWithPersonalRating(
            movie = toMovie(
                overview = entry.overview,
                ratingCount = entry.ratingCount,
                ratingAverage = entry.ratingAverage,
                releaseDate = entry.releaseDate,
                title = entry.title,
                tmdbId = entry.tmdbId
            ),
            personalRating = rating
        )
    }

    fun toDatabaseMovie(movie: Movie) = DatabaseMovie(
        overview = movie.overview,
        ratingCount = movie.rating.voteCount.toLong(),
        ratingAverage = movie.rating.average.value,
        releaseDate = movie.releaseDate.orNull(),
        title = movie.title,
        tmdbId = movie.tmdbId.toDatabaseId()
    )
}
