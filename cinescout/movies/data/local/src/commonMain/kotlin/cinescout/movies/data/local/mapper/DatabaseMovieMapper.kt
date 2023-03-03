package cinescout.movies.data.local.mapper

import arrow.core.NonEmptyList
import arrow.core.Option
import arrow.core.valueOr
import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseMovieWithPersonalRating
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.model.getOrThrow
import com.soywiz.klock.Date
import org.koin.core.annotation.Factory

@Factory
class DatabaseMovieMapper {

    @Suppress("LongParameterList")
    fun toMovie(
        backdropPath: String?,
        overview: String,
        posterPath: String?,
        ratingCount: Long,
        ratingAverage: Double,
        releaseDate: Date?,
        title: String,
        tmdbId: DatabaseTmdbMovieId
    ) = Movie(
        backdropImage = Option.fromNullable(backdropPath).map(::TmdbBackdropImage),
        overview = overview,
        posterImage = Option.fromNullable(posterPath).map(::TmdbPosterImage),
        rating = PublicRating(
            voteCount = ratingCount.toInt(),
            average = Rating.of(ratingAverage).getOrThrow()
        ),
        releaseDate = Option.fromNullable(releaseDate),
        title = title,
        tmdbId = tmdbId.toId()
    )

    fun toMovie(databaseMovie: DatabaseMovie): Movie = toMovie(
        backdropPath = databaseMovie.backdropPath,
        overview = databaseMovie.overview,
        posterPath = databaseMovie.posterPath,
        ratingCount = databaseMovie.ratingCount,
        ratingAverage = databaseMovie.ratingAverage,
        releaseDate = databaseMovie.releaseDate,
        title = databaseMovie.title,
        tmdbId = databaseMovie.tmdbId
    )

    fun toMoviesWithRating(list: List<DatabaseMovieWithPersonalRating>): List<MovieWithPersonalRating> =
        list.map { entry ->
            val rating = Rating.of(entry.personalRating).getOrThrow()

            MovieWithPersonalRating(
                movie = toMovie(
                    backdropPath = entry.backdropPath,
                    overview = entry.overview,
                    posterPath = entry.posterPath,
                    ratingCount = entry.ratingCount,
                    ratingAverage = entry.ratingAverage,
                    releaseDate = entry.releaseDate,
                    tmdbId = entry.tmdbId,
                    title = entry.title
                ),
                personalRating = rating
            )
        }

    fun toMoviesWithRating(
        list: NonEmptyList<DatabaseMovieWithPersonalRating>
    ): NonEmptyList<MovieWithPersonalRating> = list.map { entry ->
        val rating = Rating.of(entry.personalRating)
            .valueOr { throw IllegalStateException("Invalid rating: $it") }

        MovieWithPersonalRating(
            movie = toMovie(
                backdropPath = entry.backdropPath,
                overview = entry.overview,
                posterPath = entry.posterPath,
                ratingCount = entry.ratingCount,
                ratingAverage = entry.ratingAverage,
                releaseDate = entry.releaseDate,
                tmdbId = entry.tmdbId,
                title = entry.title
            ),
            personalRating = rating
        )
    }

    fun toDatabaseMovie(movie: Movie) = DatabaseMovie(
        backdropPath = movie.backdropImage.map { it.path }.orNull(),
        overview = movie.overview,
        posterPath = movie.posterImage.map { it.path }.orNull(),
        ratingCount = movie.rating.voteCount.toLong(),
        ratingAverage = movie.rating.average.value,
        releaseDate = movie.releaseDate.orNull(),
        title = movie.title,
        tmdbId = movie.tmdbId.toDatabaseId()
    )
}
