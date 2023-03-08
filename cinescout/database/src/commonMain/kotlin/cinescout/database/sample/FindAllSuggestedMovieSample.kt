package cinescout.database.sample

import cinescout.database.movie.FindAllSuggested

object FindAllSuggestedMovieSample {

    val Inception = FindAllSuggested(
        affinity = DatabaseSuggestionSample.Inception.affinity,
        backdropPath = DatabaseMovieSample.Inception.backdropPath,
        overview = DatabaseMovieSample.Inception.overview,
        posterPath = DatabaseMovieSample.Inception.posterPath,
        ratingAverage = DatabaseMovieSample.Inception.ratingAverage,
        ratingCount = DatabaseMovieSample.Inception.ratingCount,
        releaseDate = DatabaseMovieSample.Inception.releaseDate,
        source = DatabaseSuggestionSample.Inception.source,
        title = DatabaseMovieSample.Inception.title,
        tmdbId = DatabaseMovieSample.Inception.tmdbId
    )

    val TheWolfOfWallStreet = FindAllSuggested(
        affinity = DatabaseSuggestionSample.TheWolfOfWallStreet.affinity,
        backdropPath = DatabaseMovieSample.TheWolfOfWallStreet.backdropPath,
        overview = DatabaseMovieSample.TheWolfOfWallStreet.overview,
        posterPath = DatabaseMovieSample.TheWolfOfWallStreet.posterPath,
        ratingAverage = DatabaseMovieSample.TheWolfOfWallStreet.ratingAverage,
        ratingCount = DatabaseMovieSample.TheWolfOfWallStreet.ratingCount,
        releaseDate = DatabaseMovieSample.TheWolfOfWallStreet.releaseDate,
        source = DatabaseSuggestionSample.TheWolfOfWallStreet.source,
        title = DatabaseMovieSample.TheWolfOfWallStreet.title,
        tmdbId = DatabaseMovieSample.TheWolfOfWallStreet.tmdbId
    )

    val War = FindAllSuggested(
        affinity = DatabaseSuggestionSample.War.affinity,
        backdropPath = DatabaseMovieSample.War.backdropPath,
        overview = DatabaseMovieSample.War.overview,
        posterPath = DatabaseMovieSample.War.posterPath,
        ratingAverage = DatabaseMovieSample.War.ratingAverage,
        ratingCount = DatabaseMovieSample.War.ratingCount,
        releaseDate = DatabaseMovieSample.War.releaseDate,
        source = DatabaseSuggestionSample.War.source,
        title = DatabaseMovieSample.War.title,
        tmdbId = DatabaseMovieSample.War.tmdbId
    )
}
