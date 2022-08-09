package cinescout.database.mapper

import arrow.core.NonEmptyList
import cinescout.database.FindAllWithPersonalRating
import cinescout.database.model.DatabaseMovieWithPersonalRating

fun List<FindAllWithPersonalRating>.groupAsMovieWithRating(): DatabaseMovieWithPersonalRating {
    check(groupBy { it.tmdbId }.size == 1) { "List of movies must be grouped by tmdbId" }
    val movieInfo = first()
    return DatabaseMovieWithPersonalRating(
        backdropPath = movieInfo.backdropPath,
        posterPath = movieInfo.posterPath,
        ratingAverage = movieInfo.ratingAverage,
        personalRating = movieInfo.personalRating,
        ratingCount = movieInfo.ratingCount,
        releaseDate = movieInfo.releaseDate,
        title = movieInfo.title,
        tmdbId = movieInfo.tmdbId
    )
}

fun List<FindAllWithPersonalRating>.groupAsMoviesWithRating(): List<DatabaseMovieWithPersonalRating> =
    groupBy { it.tmdbId }.values.map { it.groupAsMovieWithRating() }

fun NonEmptyList<FindAllWithPersonalRating>.groupAsMoviesWithRating(): NonEmptyList<DatabaseMovieWithPersonalRating> =
    NonEmptyList.fromListUnsafe(groupBy { it.tmdbId }.values.map { it.groupAsMovieWithRating() })
