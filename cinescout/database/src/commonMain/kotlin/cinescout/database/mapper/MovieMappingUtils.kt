package cinescout.database.mapper

import cinescout.database.model.DatabaseMovieWithPersonalRating
import cinescout.database.movie.FindAllWithPersonalRating

fun List<FindAllWithPersonalRating>.groupAsMovieWithRating(): DatabaseMovieWithPersonalRating {
    check(groupBy { it.tmdbId }.size == 1) { "List of movies must be grouped by tmdbId" }
    val movieInfo = first()
    return DatabaseMovieWithPersonalRating(
        backdropPath = movieInfo.backdropPath,
        overview = movieInfo.overview,
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
