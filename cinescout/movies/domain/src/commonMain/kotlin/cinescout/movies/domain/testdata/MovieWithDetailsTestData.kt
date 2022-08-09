package cinescout.movies.domain.testdata

import arrow.core.nonEmptyListOf
import cinescout.movies.domain.model.MovieWithDetails

object MovieWithDetailsTestData {

    val Inception = MovieWithDetails(
        movie = MovieTestData.Inception,
        genres = nonEmptyListOf(GenreTestData.Action, GenreTestData.Adventure, GenreTestData.ScienceFiction)
    )

    val TheWolfOfWallStreet = MovieWithDetails(
        movie = MovieTestData.TheWolfOfWallStreet,
        genres = nonEmptyListOf(GenreTestData.Action, GenreTestData.Crime, GenreTestData.Drama)
    )

    val War = MovieWithDetails(
        movie = MovieTestData.War,
        genres = nonEmptyListOf(GenreTestData.Action, GenreTestData.Adventure, GenreTestData.ScienceFiction)
    )
}
